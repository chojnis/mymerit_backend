package com.mymerit.mymerit.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("feedback")
@Data
public class Feedback {
    @Id
    private String id;

    @DBRef
    @JsonIgnore
    private Solution solution;

    @NotBlank
    private List<String> files;

    private String comment;

    @NotNull
    @Min(0)
    @Max(9999)
    private Integer credits;

    private LocalDateTime submitDate;

    public Feedback(Solution solution, List<String> files, Integer credits, String comment) {
        this.solution = solution;
        this.files = files;
        this.credits = credits;
        this.comment = comment;
        this.submitDate = LocalDateTime.now();
    }
}
