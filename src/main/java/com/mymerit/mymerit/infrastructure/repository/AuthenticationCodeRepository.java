package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.AuthenticationCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthenticationCodeRepository extends MongoRepository<AuthenticationCode, String> {
    AuthenticationCode findByEmail(String email);
    List<AuthenticationCode> findAllByEmail(String email);
    AuthenticationCode findByCode(int code);
    Boolean existsByEmail(String email);
    Boolean existsByCode(int code);
    void deleteByEmail(String email);
    void deleteByCode(int code);
}