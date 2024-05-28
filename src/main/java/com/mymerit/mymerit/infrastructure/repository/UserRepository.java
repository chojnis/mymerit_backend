package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String username);
    Optional<User> findByProviderId(String providerId);

    Boolean existsByEmail(String email);
}