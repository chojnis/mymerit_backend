package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Reward;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RewardRepository extends MongoRepository<Reward, String> {
}