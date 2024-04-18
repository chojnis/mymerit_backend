package com.mymerit.mymerit.domain.entity;

import com.mymerit.mymerit.domain.models.TaskStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Document("tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {
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
    private Set<String> allowedLanguages;

    private Integer memoryLimit;

    private Float timeLimit;

    public List<CodeTest> tests;

    private List<Solution> solutions = new ArrayList<>();

    public Task(String title, String instructions, LocalDateTime opensAt, LocalDateTime closesAt, Integer reward, Set<String> allowedLanguages, String testSolution,
                String input, String output) {
        this.title = title;
        this.instructions = instructions;
        this.opensAt = opensAt;
        this.closesAt = closesAt;
        this.reward = reward;
        this.allowedLanguages = allowedLanguages;


    }

    public Integer getSolutionCount() {
        return solutions.size();
    }

    //@AccessType(PROPERTY)
    public TaskStatus getStatus() {
        LocalDateTime now = LocalDateTime.now();

        if (this.getClosesAt().isBefore(now)) {
            return TaskStatus.EXPIRED;
        } else if (this.getOpensAt().isAfter(now)) {
            return TaskStatus.NOT_YET_OPEN;
        } else {
            return TaskStatus.OPEN;
        }
    }

    public void addSolution(Solution solution){
        if(getStatus() == TaskStatus.OPEN) {
            solutions.add(solution);
        }
    }

    public Solution getSolutionForUser(User user){
        return this.solutions.stream().filter(solution -> solution.getUser().equals(user))
                .findFirst()
                .orElse(null);
    }

    Map<String,String> encodeData(String input, String output){

        String encodedInput = Base64.getEncoder().encodeToString(input.getBytes());
        String encodedOutput = Base64.getEncoder().encodeToString(output.getBytes());

        Map<String, String> result = new HashMap<>();
        result.put(encodedInput, encodedOutput);
        return result;
    }
}
