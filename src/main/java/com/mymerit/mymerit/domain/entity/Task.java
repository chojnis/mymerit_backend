package com.mymerit.mymerit.domain.entity;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document("tasks")
public class Task {

    @Id
    private String id;

    @NotNull
    private String topic;

    @NotNull
    private String description;

    private LocalDateTime releaseDate;

    @NotNull
    private LocalDateTime expiryDate;

    @NotNull
    private Integer reward;

    @NotNull
    private Company company;

    @NotEmpty
    private List<String> allowedTechnologies;

    private List<Solution> solutions;

    private Integer timeLeft;

    public Task(String topic, String description,LocalDateTime releaseDate, LocalDateTime expiryDate, Integer reward, Company company, List<String> allowedTechnologies) {
        this.topic = topic;
        this.description = description;
        this.releaseDate = releaseDate;
        this.expiryDate = expiryDate;
        this.company = company;
        this.allowedTechnologies = allowedTechnologies;
        this.reward = reward;
        this.solutions = new ArrayList<>();
    }

    public Integer getSolutionCount() {
        return solutions.size();
    }

    public void addSolution(Solution solution){
        solutions.add(solution);
    }
}
