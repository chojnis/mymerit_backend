package com.mymerit.mymerit.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mymerit.mymerit.domain.models.AuthProvider;
import com.mymerit.mymerit.domain.models.ProgrammingLanguage;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document("users")
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private String imageBase64;
    private AuthProvider provider;
    private String providerId;
    private Integer credits = 0;
    private String description;
    private String role;
    private List<Badge> badges = new ArrayList<>();

    public void addBadgeOrIncrementExisting(ProgrammingLanguage language) {
        boolean badgeExists = false;

        for (Badge x : badges) {
            if (x.getLanguage() == language) {
                x.incrementTasksCounter();
                badgeExists = true;
                break;
            }
        }
        if (!badgeExists) {
            Badge badge = new Badge(language);
            badge.incrementTasksCounter();
            badges.add(badge);
        }
    }

}

