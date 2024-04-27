package com.mymerit.mymerit.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("rewards-history")
public class RewardHistory {
    @Id
    private String id;
    @DBRef
    private Reward reward;
    @DBRef
    private User user;
    private LocalDateTime datePurchase;
}