package com.mymerit.mymerit.domain.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

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
    private List<String> requiredSkills;

    @NotEmpty(message = "Preferred skills are required")
    private List<String> preferredSkills;

    @NotEmpty(message = "Work locations are required")
    private List<String> workLocations;

    @NotEmpty(message = "Technologies are required")
    private List<String> technologies;

    @NotNull(message = "Experience is required")
    private Experience experience;

    @NotNull(message = "Company is required")
    @Valid
    private Company company;

    @NotNull(message = "Task is required")
    @Valid
    private Task task;

    public JobOffer(String jobTitle, String description, List<String> requiredSkills, List<String> preferredSkills, List<String> workLocations, List<String> technologies, Company company, Task task) {
        this.jobTitle = jobTitle;
        this.description = description;
        this.requiredSkills = requiredSkills;
        this.preferredSkills = preferredSkills;
        this.workLocations = workLocations;
        this.technologies = technologies;
        this.company = company;
        this.task = task;
    }
}


enum Experience{
    INTERN, JUNIOR, REGULAR, SENIOR, EXPERT
}