package com.mymerit.mymerit.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Getter
@Setter
@Document("solutions")
public class Solution {
    @Id
    public String id;

    @JsonIgnore
    public Task taskId;

    @JsonIgnore
    public User user;

    public List<String> files;


    public Solution(Task taskId, User user, List<String> files) {
        this.taskId = taskId;
        this.user = user;
        this.files = files;
    }
}
