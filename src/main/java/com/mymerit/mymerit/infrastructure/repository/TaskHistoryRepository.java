package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Task;
import com.mymerit.mymerit.domain.entity.TaskHistory;
import com.mymerit.mymerit.domain.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskHistoryRepository extends MongoRepository<Task, String> {
    Optional<List<TaskHistory>> findByUser(User user);
}