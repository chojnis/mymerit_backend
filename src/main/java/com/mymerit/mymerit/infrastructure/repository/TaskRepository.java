package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Solution;
import com.mymerit.mymerit.domain.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.PriorityQueue;

public interface TaskRepository extends MongoRepository<Task, String> {


    Page<Task> findAllByOrderByExpiryDateDesc(Pageable pageable);
    Page<Task> findAllByOrderByRewardDesc(Pageable pageable);
    //Page<Task> findByAllowedTechnologiesInAndMinCreditsLessThanEqualAndMaxCreditsGreaterThanEqualAndTimeLeftLessThanEqual(
           //List<String> languages, Integer minCredits, Integer maxCredits, Integer timeLeft, Pageable pageable);

    Page<Task> findAll(
           Pageable pageable
    );



}



