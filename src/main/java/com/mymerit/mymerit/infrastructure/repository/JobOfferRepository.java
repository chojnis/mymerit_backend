package com.mymerit.mymerit.infrastructure.repository;


import com.mymerit.mymerit.domain.entity.JobOffer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface JobOfferRepository extends MongoRepository<JobOffer, String> {

    Optional<JobOffer> findById(String id);

    Page<JobOffer> findByJobTitleContainingIgnoreCaseAndSalaryBetweenAndTaskRewardBetweenAndTaskOpensAtBetween(
            String jobTitle,
            Range<Integer> salaryRange,
            Range<Integer> creditsRange,
            Range<Date> dateRange,
            Pageable pageable
    );

    Page<JobOffer> findByJobTitleContainingIgnoreCaseAndTaskAllowedLanguagesInIgnoreCaseAndSalaryBetweenAndTaskRewardBetweenAndTaskOpensAtBetween(
            String jobTitle,
            Set<String> languages,
            Range<Integer> salaryRange,
            Range<Integer> creditsRange,
            Range<Date> dateRange,
            Pageable pageable
    );


}
