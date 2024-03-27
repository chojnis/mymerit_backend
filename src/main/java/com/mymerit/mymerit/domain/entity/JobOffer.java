package com.mymerit.mymerit.domain.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document("job_offers")
public class JobOffer {
    @Id
    private String id;

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

    @NotNull(message = "Company is required")
    @Valid
    private Company company;

    @NotNull(message = "Task is required")

    @Valid
    private Task task;

    @NotNull(message = "Salary is required")
    private Integer salary;

    @NotNull(message = "Employment Type is required")
    private EmploymentType mode;

    public JobOffer(String jobTitle, String description, Set<String> requiredSkills, Set<String> preferredSkills, Set<String> workLocations, Set<String> technologies, Company company, Task task, Experience1 experience, EmploymentType mode, Integer salary) {
        this.jobTitle = jobTitle;
        this.description = description;
        this.requiredSkills = requiredSkills;
        this.preferredSkills = preferredSkills;
        this.workLocations = workLocations;
        this.technologies = technologies;
        this.company = company;
        this.task = task;
        this.experience = experience;
        this.mode = mode;
        this.salary = salary;

    }

}


