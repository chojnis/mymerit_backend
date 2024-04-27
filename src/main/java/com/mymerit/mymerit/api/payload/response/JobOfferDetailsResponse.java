package com.mymerit.mymerit.api.payload.response;

import com.mymerit.mymerit.domain.entity.User;
import com.mymerit.mymerit.domain.models.EmploymentType;
import com.mymerit.mymerit.domain.models.Experience1;
import com.mymerit.mymerit.domain.models.TaskStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobOfferDetailsResponse {
    @NotNull
    private String id;

    @NotBlank
    private String jobTitle;

    @NotBlank
    private String description;

    @NotEmpty
    private Set<String> requiredSkills;

    @NotEmpty
    private Set<String> preferredSkills;

    @NotEmpty
    private Set<String> workLocations;

    @NotEmpty
    private Set<String> technologies;

    @NotNull
    @Valid
    private User company;

    @NotNull
    private UserTaskDetailsResponse task;

    @NotEmpty
    private Integer salary;

    @NotNull
    private Experience1 experience;

    @NotNull
    private EmploymentType employmentType;

    @NotNull
    private LocalDateTime opensAt;

    @NotNull
    private LocalDateTime closesAt;

    @NotNull
    private TaskStatus status;
}