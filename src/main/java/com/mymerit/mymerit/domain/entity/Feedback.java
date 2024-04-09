package com.mymerit.mymerit.domain.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("feedback")
@Data
public class Feedback {
    @Id
    private String id;

    @DBRef
    private Solution solution;

    @NotBlank
    private String content;

    @NotNull
    @Min(0)
    @Max(9999)
    private Integer credits;
}
