package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.RewardHistory;
import com.mymerit.mymerit.domain.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardHistoryRepository extends MongoRepository<RewardHistory, String> {
    Optional<List<RewardHistory>> findByUser(User user);
}