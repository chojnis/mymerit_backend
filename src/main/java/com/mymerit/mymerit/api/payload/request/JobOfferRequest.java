package com.mymerit.mymerit.api.payload.request;

import com.mymerit.mymerit.domain.entity.Task;
import com.mymerit.mymerit.domain.models.EmploymentType;
import com.mymerit.mymerit.domain.models.Experience1;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class JobOfferRequest{

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotBlank(message = "Description is required")
    private String description;

    @NotEmpty(message = "Required skills are required")
    private Set<String> requiredSkills;

    @NotEmpty(message = "Preferred skills are required")
    private Set<String> preferredSkills;

    @NotEmpty(message = "Work locations are required")
    private Set<String> workLocations;

    @NotEmpty(message = "Technologies are required")
    private Set<String> technologies;

    @NotNull(message = "Experience is required")
    private Experience1 experience;

    @NotNull(message = "Task is required")
    private Task task;

    @NotNull(message = "Salary is required")
    private Integer salary;

    @NotNull(message = "Employment Type is required")
    private EmploymentType employmentType;
}