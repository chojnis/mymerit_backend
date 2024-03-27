package com.mymerit.mymerit.api.payload.request;

import com.mymerit.mymerit.domain.entity.Company;
import com.mymerit.mymerit.domain.entity.Solution;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

public class TaskRequest {


    @NotNull   public String topic;

    @NotNull   public String description;

    public LocalDateTime releaseDate;

    public LocalDateTime expiryDate;

    @NotNull   public Integer reward;

    @NotNull   public Company company;

    public Integer solutionCount;

    @NotEmpty
    public List<String> allowedTechnologies;

    public Integer maxCredits;

    public Integer minCredits;
}
