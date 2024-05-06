package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.request.EmailVerification;
import com.mymerit.mymerit.api.payload.request.SignUpRequest;
import com.mymerit.mymerit.api.payload.response.ApiResponse;
import com.mymerit.mymerit.domain.entity.User;
import com.mymerit.mymerit.domain.models.AuthProvider;
import com.mymerit.mymerit.domain.models.Role;
import com.mymerit.mymerit.domain.service.VerificationCodeService;
import com.mymerit.mymerit.infrastructure.repository.AuthenticationCodeRepository;
import com.mymerit.mymerit.infrastructure.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class SignUpController {
    private final UserRepository userRepository;
    private final AuthenticationCodeRepository authenticationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeService verificationCodeService;

    public SignUpController(UserRepository userRepository, AuthenticationCodeRepository authenticationCodeRepository, PasswordEncoder passwordEncoder, VerificationCodeService verificationCodeService) {
        this.userRepository = userRepository;
        this.authenticationCodeRepository = authenticationCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationCodeService = verificationCodeService;
    }

    @PostMapping("/code")
    public ResponseEntity<?> emailVerification(@RequestParam(name="verify", required = false) String code, @Valid @RequestBody EmailVerification emailVerification) {
        if (userRepository.existsByEmail(emailVerification.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "Email address already in use"));
        }

        if (code != null) {
            return verificationCodeService.processVerificationCode(code, emailVerification.getEmail());
        }

        if (authenticationCodeRepository.existsByEmail(emailVerification.getEmail())) {
            return verificationCodeService.checkIfCodeAlreadySent(emailVerification.getEmail());
        }

        return verificationCodeService.generateAndSendVerificationCode(emailVerification.getEmail());
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "Email address already in use"));
        }

        int codeInt = Integer.parseInt(signUpRequest.getCode());

        if (!authenticationCodeRepository.existsByCode(codeInt)) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "The verification code is invalid"));
        }

        if (authenticationCodeRepository.findByCode(codeInt).getEmail().equals(signUpRequest.getEmail())) {
            if (LocalDateTime.now().isAfter(authenticationCodeRepository.findByCode(Integer.parseInt(signUpRequest.getCode())).getExpiration())) {
                return ResponseEntity
                        .badRequest()
                        .body(new ApiResponse(false, "Verification code expired"));
            }
        }

        User user = new User();

        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProvider(AuthProvider.local);
        user.setRole(Role.user.name());

        User result = userRepository.insert(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, "User registered successfully"));
    }

    @PostMapping("/sign-up-company")
    public ResponseEntity<?> registerCompany(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "Email address already in use"));
        }

        User user = new User();

        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProvider(AuthProvider.local);
        user.setRole(Role.company.name());

        User result = userRepository.insert(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, "User (company) registered successfully"));
    }
}