package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Task;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {


    Page<Task> findAllByAllowedTechnologiesContainingIgnoreCaseAndRewardBetween(
            @NotEmpty List<String> allowedTechnologies,
            Range<Integer> range,
            Pageable pageable
    );

    Page<Task> findAllByRewardBetween(
            Range<Integer> range,
            Pageable pageable
    );




}



