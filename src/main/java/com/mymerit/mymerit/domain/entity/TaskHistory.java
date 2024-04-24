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
    @DBRef
    private Task task;
    @DBRef
    private User user;
    private LocalDateTime dateLastModified;
}
