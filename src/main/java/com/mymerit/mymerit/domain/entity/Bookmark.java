package com.mymerit.mymerit.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Bookmark {
    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private JobOffer jobOffer;

    public Bookmark(User userId, JobOffer jobId) {
        this.user = userId;
        this.jobOffer = jobId;
    }
}