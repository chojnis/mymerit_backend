package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Solution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;;

@Repository
public interface SolutionRepository extends MongoRepository<Solution, String> {
    List<Solution> findAllByUserId(String userId);
}
