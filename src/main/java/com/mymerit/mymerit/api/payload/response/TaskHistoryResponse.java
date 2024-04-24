package com.mymerit.mymerit.api.payload.response;

import com.mymerit.mymerit.domain.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class TaskHistoryResponse {
    private Task task;
    private LocalDateTime dateLastModified;
}