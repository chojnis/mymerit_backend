package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Solution;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolutionRepository extends MongoRepository<Solution, String> {
}
