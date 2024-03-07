package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Solution;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SolutionRepository extends MongoRepository<Solution, String> {

    public Optional<Solution> getSolutionById(String idTask, String idSolution);






}
