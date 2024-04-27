package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.JobOfferHistory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOfferHistoryRepository extends MongoRepository<JobOfferHistory, String> {
    Optional<List<JobOfferHistory>> findByUserId(String userId);
}
