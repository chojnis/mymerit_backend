package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.request.SolutionRequest;
import com.mymerit.mymerit.api.payload.response.JobOfferDetailsResponse;
import com.mymerit.mymerit.api.payload.response.JobOfferListResponse;
import com.mymerit.mymerit.domain.entity.JobOffer;
import com.mymerit.mymerit.domain.entity.Solution;
import com.mymerit.mymerit.domain.entity.Task;
import com.mymerit.mymerit.domain.entity.User;
import com.mymerit.mymerit.infrastructure.repository.JobOfferRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class JobOfferService {

    JobOfferRepository jobOfferRepository;

    JobOfferService(JobOfferRepository jobOfferRepository){
        this.jobOfferRepository = jobOfferRepository;
    }


    public JobOffer addJobOffer(JobOffer jobOffer){

        Integer companyCredits = jobOffer.getCompany().getCreditsAmount();

        if(companyCredits > jobOffer.getTask().getReward()) {

            jobOffer.getCompany().setCreditsAmount(companyCredits - jobOffer.getTask().getReward());

            return jobOfferRepository.save(jobOffer);
        }

        return null;
    }

    public Optional<JobOffer> getById(String id){
        return jobOfferRepository.findById(id);
    }

    public Optional<JobOfferDetailsResponse> getJobOfferDetailsResponse(String id) {
        return jobOfferRepository.findById(id)
                .map(this::createJobOfferDetailsResponse);
    }

    private JobOfferDetailsResponse createJobOfferDetailsResponse(JobOffer jobOffer) {
        Task task = jobOffer.getTask().isOpen() ? jobOffer.getTask() : null;
        return new JobOfferDetailsResponse(
                jobOffer.getId(),
                jobOffer.getJobTitle(),
                jobOffer.getDescription(),
                jobOffer.getRequiredSkills(),
                jobOffer.getPreferredSkills(),
                jobOffer.getWorkLocations(),
                jobOffer.getTechnologies(),
                jobOffer.getCompany(),
                jobOffer.getTask(),
                jobOffer.getSalary()


        );
    }

    private JobOfferListResponse createJobOfferListResponse(JobOffer jobOffer){

        System.out.println("imhere");
        return new JobOfferListResponse(

                jobOffer.getId(),
                jobOffer.getJobTitle(),
                jobOffer.getWorkLocations(),
                jobOffer.getTechnologies(),
                jobOffer.getTask().getReward(),
                jobOffer.getTask().getOpensAt(),
                jobOffer.getTask().getClosesAt(),
                jobOffer.getCompany(),
                jobOffer.getSalary()

        );
    }

    public Page<JobOfferListResponse> getJobOffers(List<String> technologies, Range<Integer> salaryRange, Range<Integer> creditsRange, Pageable pageable) {


        return jobOfferRepository.findAllByTechnologiesContainingIgnoreCaseAndSalaryBetweenAndTaskRewardBetween(technologies,salaryRange,creditsRange, pageable)
                .map(this::createJobOfferListResponse);
    }



}
