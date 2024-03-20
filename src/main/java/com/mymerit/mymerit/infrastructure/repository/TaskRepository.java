package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
}
