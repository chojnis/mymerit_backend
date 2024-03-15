package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.request.SolutionRequest;
import com.mymerit.mymerit.api.payload.response.JobOfferDetailsResponse;
import com.mymerit.mymerit.api.payload.response.JobOfferListResponse;
import com.mymerit.mymerit.domain.entity.JobOffer;
import com.mymerit.mymerit.domain.entity.Solution;
import com.mymerit.mymerit.domain.entity.Task;
import com.mymerit.mymerit.domain.entity.User;
import com.mymerit.mymerit.infrastructure.repository.JobOfferRepository;
import com.mymerit.mymerit.infrastructure.repository.UserRepository;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class JobOfferService {
    JobOfferRepository jobOfferRepository;
    UserRepository userRepository;

    JobOfferService(JobOfferRepository jobOfferRepository,UserRepository userRepository){
        this.jobOfferRepository = jobOfferRepository;
        this.userRepository = userRepository;
    }

    public JobOffer addJobOffer(JobOffer jobOffer){
        Integer companyCredits = jobOffer.getCompany().getCreditsAmount();

        if(companyCredits > jobOffer.getTask().getReward()) {
            jobOffer.getCompany().setCreditsAmount(companyCredits - jobOffer.getTask().getReward());
            return jobOfferRepository.save(jobOffer);
        }

        return null;
    }

    public Optional<JobOfferDetailsResponse> getJobOfferDetailsResponse(String id) {
        return jobOfferRepository.findById(id)
                .map(this::createJobOfferDetailsResponse);
    }

    private JobOfferDetailsResponse createJobOfferDetailsResponse(JobOffer jobOffer) {
        return new JobOfferDetailsResponse(
                jobOffer.getId(),
                jobOffer.getJobTitle(),
                jobOffer.getDescription(),
                jobOffer.getRequiredSkills(),
                jobOffer.getPreferredSkills(),
                jobOffer.getWorkLocations(),
                jobOffer.getTechnologies(),
                jobOffer.getCompany(),
                jobOffer.getTask().isOpen() ? jobOffer.getTask() : null,
                jobOffer.getSalary(),
                jobOffer.getExperience(),
                jobOffer.getMode(),
                jobOffer.getTask().getOpensAt(),
                jobOffer.getTask().getClosesAt()
        );
    }

    private JobOfferListResponse createJobOfferListResponse(JobOffer jobOffer){
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

    public Page<JobOfferListResponse> getJobOffers(Set<String> languages, Range<Integer> salaryRange, Range<Integer> creditsRange, Pageable pageable) {
        return jobOfferRepository.findAllByTaskAllowedLanguagesContainingIgnoreCaseAndSalaryBetweenAndTaskRewardBetween(languages,salaryRange,creditsRange, pageable)
                .map(this::createJobOfferListResponse);
    }


    public JobOffer addSolution(String jobOfferId,SolutionRequest solutionRequest, String userId){



        if(jobOfferRepository.findById(jobOfferId).isPresent()) {
            Task task = jobOfferRepository.findById(jobOfferId).get().getTask();
            Solution solution = new Solution(task,userRepository.findById(userId).get(),solutionRequest.getFiles());

            JobOffer jobOffer = jobOfferRepository.findById(jobOfferId).get();
            jobOffer.getTask().addSolution(solution);

            return jobOfferRepository.save(jobOffer);

        }

        else return null;
    }


}
