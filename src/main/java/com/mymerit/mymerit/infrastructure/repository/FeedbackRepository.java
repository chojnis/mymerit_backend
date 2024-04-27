package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Feedback;
import com.mymerit.mymerit.domain.entity.Solution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {
}
