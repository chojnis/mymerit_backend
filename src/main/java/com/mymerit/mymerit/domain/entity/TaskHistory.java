package com.mymerit.mymerit.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("tasks-history")
public class TaskHistory {
    @Id
    private String id;
    private String userId;
    @DBRef
    private Task task;
    private LocalDateTime dateLastModified;

    public TaskHistory(Task task, String userId, LocalDateTime dateLastModified){
        this.task = task;
        this.userId = userId;
        this.dateLastModified = dateLastModified;
    }
}
