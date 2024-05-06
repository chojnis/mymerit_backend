package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.response.ApiResponse;
import com.mymerit.mymerit.domain.entity.AuthenticationCode;
import com.mymerit.mymerit.infrastructure.repository.AuthenticationCodeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerificationCodeService {
    private final AuthenticationCodeRepository authenticationCodeRepository;
    private final MailSenderService mailSenderService;

    public VerificationCodeService(AuthenticationCodeRepository authenticationCodeRepository, MailSenderService mailSenderService) {
        this.authenticationCodeRepository = authenticationCodeRepository;
        this.mailSenderService = mailSenderService;
    }

    public ResponseEntity<?> processVerificationCode(String code, String email) {

        List<AuthenticationCode> codes = authenticationCodeRepository.findAllByEmail(email);

        if (codes.stream().noneMatch(c -> c.getCode() == code)) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "The verification code is invalid"));
        }

        if (LocalDateTime.now().isAfter(authenticationCodeRepository.findByCode(code).getExpiration())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "Verification code expired"));
        }

        return ResponseEntity
                .ok()
                .body(new ApiResponse(true, "Verification successful"));
    }

    public ResponseEntity<?> checkIfCodeAlreadySent(String email) {
        List<AuthenticationCode> authenticationCodes = authenticationCodeRepository.findAllByEmail(email);

        for (AuthenticationCode authenticationCode : authenticationCodes) {
            if (LocalDateTime.now().isBefore(authenticationCode.getExpiration())) {
                return ResponseEntity
                        .ok()
                        .body(new ApiResponse(true, "Verification code already sent"));
            }
        }

        return generateAndSendVerificationCode(email);
    }

    public ResponseEntity<?> generateAndSendVerificationCode(String email) {
        AuthenticationCode authenticationCode = new AuthenticationCode();

        authenticationCode.setEmail(email);

        do {
            authenticationCode.setCode(Integer.toString(mailSenderService.generateVerificationCode()));
        } while (authenticationCodeRepository.existsByCode(authenticationCode.getCode()));

        authenticationCode.setExpiration(LocalDateTime.now().plusMinutes(30));

        authenticationCodeRepository.insert(authenticationCode);
        //mailSenderService.sendEmail(emailVerification.getEmail());

        return ResponseEntity
                .ok()
                //.body(new ApiResponse(true, "Verification code was successfully sent"))
                .body(new ApiResponse(true, "Verification code created successfully"));
    }
}