package com.mymerit.mymerit.domain.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("authentication-codes")
public class AuthenticationCode {
    @Id
    private String id;
    @NotBlank
    private String email;
    @NotBlank
    private int code;
    @NotBlank
    private LocalDateTime expiration;
}