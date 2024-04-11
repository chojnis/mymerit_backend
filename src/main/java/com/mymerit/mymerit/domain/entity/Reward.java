package com.mymerit.mymerit.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("rewards")
public class Reward {
    @Id
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer cost;
}