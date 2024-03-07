package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company,String> {
}
