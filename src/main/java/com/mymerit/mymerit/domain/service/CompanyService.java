package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.domain.entity.Company;
import com.mymerit.mymerit.infrastructure.repository.CompanyRepository;

import java.util.Optional;

public class CompanyService {

    CompanyRepository companyRepository;

    CompanyService(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }


    Optional<Company> findCompanyById(String id){
        return companyRepository.findById(id);

    }


}
