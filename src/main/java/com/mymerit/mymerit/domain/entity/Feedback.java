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

import java.util.List;

@Document("feedback")
@Data
public class Feedback {
    @Id
    private String id;

    @DBRef
    @JsonIgnore
    private Solution solutionId;

    @NotBlank
    private List<String> files;

    @NotNull
    @Min(0)
    @Max(9999)
    private Integer credits;

    public Feedback(Solution solutionId, List<String> files, Integer credits) {
        this.solutionId = solutionId;
        this.files = files;
        this.credits = credits;
    }
}
