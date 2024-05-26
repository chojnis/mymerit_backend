package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.response.ApiResponse;
import com.mymerit.mymerit.domain.entity.AuthenticationCode;
import com.mymerit.mymerit.infrastructure.repository.AuthenticationCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerificationCodeService {
    private final AuthenticationCodeRepository authenticationCodeRepository;
    private final MailSenderService mailSenderService;
    private final Logger logger = LoggerFactory.getLogger(VerificationCodeService.class);

    public VerificationCodeService(AuthenticationCodeRepository authenticationCodeRepository, MailSenderService mailSenderService) {
        this.authenticationCodeRepository = authenticationCodeRepository;
        this.mailSenderService = mailSenderService;
    }

    public ResponseEntity<?> processVerificationCode(String code, String email) {
        int codeInt;
        try {
            codeInt = Integer.parseInt(code);
        } catch (Exception exception) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "The verification code is invalid"));
        }

        List<AuthenticationCode> codes = authenticationCodeRepository.findAllByEmail(email);

        if (codes.stream().noneMatch(c -> c.getCode() == codeInt)) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "The verification code is invalid"));
        }

        if (LocalDateTime.now().isAfter(authenticationCodeRepository.findByCode(codeInt).getExpiration())) {
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
            authenticationCode.setCode(mailSenderService.generateVerificationCode());
        } while (authenticationCodeRepository.existsByCode(authenticationCode.getCode()));

        authenticationCode.setExpiration(LocalDateTime.now().plusMinutes(30));

        authenticationCodeRepository.insert(authenticationCode);

        try {
            mailSenderService.sendVerificationCode(email, authenticationCode.getCode());
        } catch (Exception e) {
            logger.info("Failed to send email", e);

            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse(false, "Failed to send email"));

        }

        return ResponseEntity
                .ok()
                .body(new ApiResponse(true, "Verification code sent successfully"));
    }
}