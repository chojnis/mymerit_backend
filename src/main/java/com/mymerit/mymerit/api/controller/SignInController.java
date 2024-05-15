package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.request.SignInRequest;
import com.mymerit.mymerit.api.payload.response.ApiResponse;
import com.mymerit.mymerit.api.payload.response.JwtResponse;
import com.mymerit.mymerit.infrastructure.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "SignInController")
public class SignInController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public SignInController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Operation(){
        summary = "sign in account"
    }
    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new ApiResponse(
                true,
                "Logged in successfully",
                new JwtResponse(jwt)
        ));
    }
}