package com.mymerit.mymerit.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("jobOffer-history")
public class JobOfferHistory {
    @Id
    private String id;
    private String userId;
    @DBRef
    private JobOffer jobOffer;
    private LocalDateTime dateLastModified;

    public JobOfferHistory(JobOffer jobOffer, String userId, LocalDateTime dateLastModified){
        this.jobOffer = jobOffer;
        this.userId = userId;
        this.dateLastModified = dateLastModified;
    }
}