package com.mymerit.mymerit.api.payload.response;

import com.mymerit.mymerit.domain.entity.*;
import com.mymerit.mymerit.domain.models.ProgrammingLanguage;
import com.mymerit.mymerit.domain.models.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserTaskDetailsResponse {
    @Id
    private String id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Instructions are required")
    private String instructions;

    @NotNull(message = "Opening time is required")
    private LocalDateTime opensAt;

    @NotNull(message = "Closing time is required")
    private LocalDateTime closesAt;

    @NotNull(message = "Reward is required")
    private Integer reward;

    @NotEmpty(message = "Allowed languages are required")
    private Set<ProgrammingLanguage> allowedLanguages;

    private Integer memoryLimit;
    private Float timeLimit;

    @NotNull
    private TaskStatus status;
    private Solution userSolution;

    private Feedback companyFeedback;
//    private Map<ProgrammingLanguage, List<TemplateFile>> templateFiles;
    private List<TemplateFile> templateFiles;

    private List<CodeTest> tests;

}
