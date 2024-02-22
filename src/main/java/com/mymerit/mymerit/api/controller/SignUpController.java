package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.request.SignUpRequest;
import com.mymerit.mymerit.api.payload.response.ApiResponse;
import com.mymerit.mymerit.domain.entity.User;
import com.mymerit.mymerit.infrastructure.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class SignUpController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "Email address already in use."));
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.insert(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, "User registered successfully", null));
    }
}