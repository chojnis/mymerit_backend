package com.mymerit.mymerit.domain.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("rewards")
public class Rewards{
    @Id
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private int cost;
}