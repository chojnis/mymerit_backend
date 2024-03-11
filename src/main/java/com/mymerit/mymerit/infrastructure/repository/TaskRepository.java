package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Solution;
import com.mymerit.mymerit.domain.entity.Task;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;

public interface TaskRepository extends MongoRepository<Task, String> {


    Page<Task> findAllByAllowedTechnologiesInAndRewardBetween(
            Collection<List<String>> allowedTechnologies,
            Integer minReward,
            Integer maxReward,
            Pageable pageable
    );


    Page<Task> findAllByAllowedTechnologiesInAndRewardBetweenOrderByRewardAsc(
            Collection<List<String>> allowedTechnologies,
            Integer minReward,
            Integer maxReward,
            Pageable pageable
    );

    Page<Task> findAllByAllowedTechnologiesInAndRewardBetweenOrderByRewardDesc(
             Collection<@NotEmpty List<String>> allowedTechnologies,
             Integer minReward,
             Integer maxReward,
             Pageable pageable
    );
}



