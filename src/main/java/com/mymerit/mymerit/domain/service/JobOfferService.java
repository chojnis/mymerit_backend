package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.request.SolutionRequest;
import com.mymerit.mymerit.api.payload.response.JobOfferDetailsResponse;
import com.mymerit.mymerit.api.payload.response.JobOfferListResponse;
import com.mymerit.mymerit.domain.entity.*;
import com.mymerit.mymerit.infrastructure.repository.JobOfferRepository;
import com.mymerit.mymerit.infrastructure.repository.SolutionRepository;
import com.mymerit.mymerit.infrastructure.repository.TaskRepository;
import com.mymerit.mymerit.infrastructure.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.data.domain.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


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
                jobOffer.getTask().getTaskStatus() == TaskStatus.OPEN ? jobOffer.getTask() : null,
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

    public Page<JobOfferListResponse> getJobOffers(
            String q,
            Set<String> languages,
            Integer page,
            Integer minCredits,
            Integer maxCredits,
            Integer minSalary,
            Integer maxSalary,
            Sort sort
    ) {
        Range<Integer> salaryRange = Range.of(Range.Bound.inclusive(minSalary), Range.Bound.inclusive(maxSalary));
        Range<Integer> creditsRange = Range.of(Range.Bound.inclusive(minCredits), Range.Bound.inclusive(maxCredits));
        PageRequest pageRequest = PageRequest.of(page, 4, sort);

        if (languages.isEmpty()) {
            return jobOfferRepository.findByJobTitleContainingIgnoreCaseAndSalaryBetweenAndTaskRewardBetween(q, salaryRange, creditsRange, pageRequest)
                    .map(this::createJobOfferListResponse);
        } else {
            return jobOfferRepository.findByJobTitleContainingIgnoreCaseAndTaskAllowedLanguagesInIgnoreCaseAndSalaryBetweenAndTaskRewardBetween(q, languages, salaryRange, creditsRange, pageRequest)
                    .map(this::createJobOfferListResponse);
        }
    }


    public JobOffer addSolution(String jobOfferId, List<MultipartFile> files, String userId){
        JobOffer jobOffer = getJobOfferOrThrow(jobOfferId);
        Task task = jobOffer.getTask();

        if (userAlreadySubmittedSolution(task, userId)) {
            updateExistingSolution(task, files, userId);
        } else {
            createNewSolution(task, files, userId);
        }

        return jobOfferRepository.save(jobOffer);
    }

    private JobOffer getJobOfferOrThrow(String jobOfferId) {
        return jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> new IllegalArgumentException("Job offer not found for id: " + jobOfferId));
    }

    private boolean userAlreadySubmittedSolution(Task task, String userId) {
        return task.getSolutions().stream()
                .anyMatch(solution -> solution.getUser().getId().equals(userId));
    }

    private void updateExistingSolution(Task task, List<MultipartFile> files, String userId){
        Solution existingSolution = task.getSolutions().stream()
                .filter(solution -> solution.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Solution not found for the user"));

        List<ObjectId> fileIDs = addFiles(files);
        existingSolution.setFiles(fileIDs);
        solutionRepository.save(existingSolution);
        System.out.println("Existing solution updated: " + existingSolution);
    }

    private void createNewSolution(Task task, List<MultipartFile> files, String userId){
        List<ObjectId> fileIDs = addFiles(files);
        Solution solution = new Solution(task, getUser(userId), fileIDs);
        solutionRepository.save(solution);
        System.out.println("New solution created: " + solution);
        task.addSolution(solution);
        taskRepository.save(task);
    }

    private List<ObjectId> addFiles(List<MultipartFile> files){
        return files.stream()
                .map(file -> {
                    try {
                        return fileService.addFile(file);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to add file for solution", e);
                    }
                })
                .collect(Collectors.toList());
    }

    private User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }




}
