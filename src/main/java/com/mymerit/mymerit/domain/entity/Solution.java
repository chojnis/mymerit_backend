package com.mymerit.mymerit.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
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

    @DBRef
    @JsonIgnore
    public Task taskId;

    public User user;
    
    public List<ObjectId> files;

    public Solution(Task taskId, User user, List<ObjectId> files) {
        this.taskId = taskId;
        this.user = user;
        this.files = files;
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