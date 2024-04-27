package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Socials;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialRepository extends MongoRepository<Socials, String> {
    Optional<Socials> findByUserId(String userId);
}