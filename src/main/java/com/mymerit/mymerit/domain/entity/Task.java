package com.mymerit.mymerit.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mymerit.mymerit.domain.models.ProgrammingLanguage;
import com.mymerit.mymerit.domain.models.TaskStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
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

    @DBRef
    @JsonIgnoreProperties("task")
    private JobOffer job;

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

    private Map<ProgrammingLanguage, List<String>> templateFiles;// [language : {fileId1, fileId2},.. ]

    private Integer memoryLimit;

    private Float timeLimit;

    public List<CodeTest> tests;

    @DBRef
    private List<Solution> solutions = new ArrayList<>();

    public Task(String title, String instructions, LocalDateTime opensAt, LocalDateTime closesAt, Integer reward, Set<ProgrammingLanguage> allowedLanguages, String testSolution,
                String input, String output) {
        this.title = title;
        this.instructions = instructions;
        this.opensAt = opensAt;
        this.closesAt = closesAt;
        this.reward = reward;
        this.allowedLanguages = allowedLanguages;


    }

    public Solution findSolutionByUserId(String userId){
        for(Solution solution : solutions) {
            if (solution.getUser().getId().equals(userId)) {
                return solution;
            }
        }
        return null;
    }

    public Optional<CodeTest> getTestByLanguage(ProgrammingLanguage language){
        return tests.stream().filter(t->t.getLanguage().equals(language)).findFirst();
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

    public Optional<Solution> getSolutionForUser(String userId){
        return this.solutions.stream().filter(solution -> solution.getUser().getId().equals(userId))
                .findFirst();

    }

    Map<String,String> encodeData(String input, String output){

        String encodedInput = Base64.getEncoder().encodeToString(input.getBytes());
        String encodedOutput = Base64.getEncoder().encodeToString(output.getBytes());

        Map<String, String> result = new HashMap<>();
        result.put(encodedInput, encodedOutput);
        return result;
    }

    public Integer calculateAverageRanking(){
        Integer ranking = 0;

        for(Solution solution: solutions){
            ranking +=solution.getUser().getRanking().getAllTimeRanking();
        }

        ranking = ranking/solutions.size();
        return ranking;

    }

}
