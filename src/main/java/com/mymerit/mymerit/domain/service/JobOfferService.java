package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.request.SolutionRequest;
import com.mymerit.mymerit.api.payload.response.JobOfferDetailsResponse;
import com.mymerit.mymerit.api.payload.response.JobOfferListResponse;
import com.mymerit.mymerit.domain.entity.*;
import com.mymerit.mymerit.infrastructure.repository.JobOfferRepository;
import com.mymerit.mymerit.infrastructure.repository.SolutionRepository;
import com.mymerit.mymerit.infrastructure.repository.TaskRepository;
import com.mymerit.mymerit.infrastructure.repository.UserRepository;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
public class JobOfferService {
    JobOfferRepository jobOfferRepository;
    UserRepository userRepository;
    DownloadFileService fileService;
    TaskRepository taskRepository;
    SolutionRepository solutionRepository;

    JobOfferService(
            JobOfferRepository jobOfferRepository,
            UserRepository userRepository,
            DownloadFileService fileService,
            TaskRepository taskRepository,
            SolutionRepository solutionRepository
    ) {
        this.jobOfferRepository = jobOfferRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.taskRepository = taskRepository;
        this.solutionRepository = solutionRepository;


    }

    public JobOffer addJobOffer(JobOffer jobOffer){
        Integer companyCredits = jobOffer.getCompany().getCreditsAmount();
        taskRepository.save(jobOffer.getTask());
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
                jobOffer.getTask().getAllowedLanguages(),
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


    public JobOffer addSolution(String jobOfferId, MultipartFile [] files, String userId){

        if(jobOfferRepository.findById(jobOfferId).isPresent()) {
            Task task = jobOfferRepository.findById(jobOfferId).get().getTask();
            List<String> ids = new ArrayList<>();
            Arrays.stream(files).toList().forEach(f -> {
                try {
                    ids.add(fileService.addFile(f));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Solution solution = new Solution(task, userRepository.findById(userId).get(),ids);
            solutionRepository.save(solution);

            JobOffer jobOffer = jobOfferRepository.findById(jobOfferId).get();
            
            return jobOfferRepository.save(jobOffer);

        }

        else return null;
    }


}
