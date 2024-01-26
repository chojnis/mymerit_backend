package com.mymerit.mymerit.domain.entity;

import com.mymerit.mymerit.domain.models.Role;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class UserRole {
    @Id
    private String id;
    private Role name;
}
