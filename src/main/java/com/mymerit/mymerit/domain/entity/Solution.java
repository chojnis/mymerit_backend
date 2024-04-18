package com.mymerit.mymerit.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    private Task taskId;

    private LocalDateTime submitDate;

    private User user;

    private List<String> files;

    private Feedback feedback;


    public Solution(Task taskId, User user, List<String> files) {
        this.taskId = taskId;
        this.user = user;
        this.files = files;
        this.submitDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Solution{" +
                "id='" + id + '\'' +
                ", taskId=" + taskId.getId() +
                ", user=" + user +
                ", files=" + files +
                '}';
    }
}
