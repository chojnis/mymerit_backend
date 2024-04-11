package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Rewards;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RewardsRepository extends MongoRepository<Rewards, String> {
    
}