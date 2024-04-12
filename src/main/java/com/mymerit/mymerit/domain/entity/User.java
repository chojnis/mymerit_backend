package com.mymerit.mymerit.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mymerit.mymerit.domain.models.AuthProvider;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("users")
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private String imageUrl;

    private AuthProvider provider;

    private String providerId;

    private Integer credits = 0;

    private String description;
    private String role;
}