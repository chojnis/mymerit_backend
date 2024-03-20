package com.mymerit.mymerit.domain.entity;

import com.mymerit.mymerit.domain.models.AuthProvider;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("users")
public class User {
    @Id
    private String id;
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private String imageUrl;
    @NotBlank
    private AuthProvider provider;
    private String providerId;
    private String points;
    private String description;
    @NotBlank
    private String role;
}