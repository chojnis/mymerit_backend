package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository {
    Task findByJobOfferId(String jobOfferId);
}
