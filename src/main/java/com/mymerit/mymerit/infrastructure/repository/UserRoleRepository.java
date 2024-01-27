package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.UserRole;
import com.mymerit.mymerit.domain.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRoleRepository extends MongoRepository<UserRole, String> {
    Optional<UserRole> findByName(Role name);
}
