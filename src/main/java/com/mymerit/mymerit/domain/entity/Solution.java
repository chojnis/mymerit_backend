package com.mymerit.mymerit.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
@Getter
@Setter
@Document("solutions")
public class Solution {
    @Id
    private String id;

    @DBRef
    @JsonIgnore
    private Task task;

    private LocalDateTime submitDate;

    private User user;

    private List<String> files;

    @DBRef
    private Feedback feedback;


    public Solution(Task task, User user, List<String> files) {
        this.task = task;
        this.user = user;
        this.files = files;
        this.submitDate = LocalDateTime.now();
    }

    public Integer getSolvingTime() {
        long minutes = ChronoUnit.MINUTES.between(task.getOpensAt(), submitDate);
        return (int) minutes;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "id='" + id + '\'' +
                ", taskId=" + task.getId() +
                ", user=" + user +
                ", files=" + files +
                '}';
    }
}
