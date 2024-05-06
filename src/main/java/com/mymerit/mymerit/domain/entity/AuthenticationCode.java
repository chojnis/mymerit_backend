package com.mymerit.mymerit.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("authentication-codes")
public class AuthenticationCode {
    @Id
    private String id;
    private String email;
    private int code;
    private LocalDateTime expiration;
}