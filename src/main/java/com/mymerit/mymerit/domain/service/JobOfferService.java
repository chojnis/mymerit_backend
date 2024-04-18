package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.request.JobOfferRequest;
import com.mymerit.mymerit.api.payload.response.*;
import com.mymerit.mymerit.domain.entity.*;
import com.mymerit.mymerit.domain.models.TaskStatus;
import com.mymerit.mymerit.infrastructure.repository.JobOfferRepository;
import com.mymerit.mymerit.infrastructure.repository.SolutionRepository;
import com.mymerit.mymerit.infrastructure.repository.TaskRepository;
import com.mymerit.mymerit.infrastructure.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.*;
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

    public JobOffer addJobOffer(JobOfferRequest jobOfferRequest, UserDetailsImpl userDetails){
        User user = getUser(userDetails.getId());

        Integer userCredits = user.getCredits();
        Integer taskReward = jobOfferRequest.getTask().getReward();

        if (userCredits < taskReward) {
            throw new RuntimeException("Not enough credits to create job offer");
        }

        user.setCredits(userCredits - taskReward);
        taskRepository.save(jobOfferRequest.getTask());

        JobOffer jobOffer = new JobOffer(
                jobOfferRequest.getJobTitle(),
                jobOfferRequest.getDescription(),
                jobOfferRequest.getRequiredSkills(),
                jobOfferRequest.getPreferredSkills(),
                jobOfferRequest.getWorkLocations(),
                jobOfferRequest.getTechnologies(),
                user,
                jobOfferRequest.getTask(),
                jobOfferRequest.getExperience(),
                jobOfferRequest.getEmploymentType(),
                jobOfferRequest.getSalary()
        );

        return jobOfferRepository.save(jobOffer);
    }

    public JobOfferDetailsResponse getJobOfferDetailsResponse(String id, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userDetails.getId()));

        JobOffer jobOffer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobOffer not found with id: " + id));

        if(jobOffer.getCompany().getId().equals(user.getId())){
            return createJobOfferDetailsSolutionsResponse(jobOffer);
        }

        Optional<Solution> userSolution = jobOffer.getTask().getSolutionForUser(user);

        return createJobOfferDetailsResponse(jobOffer, userSolution.orElse(null), userSolution.map(Solution::getFeedback).orElse(null));

    }


    private JobOfferDetailsResponse createJobOfferDetailsResponse(JobOffer jobOffer, Solution userSolution, Feedback companyFeedback) {
        return new JobOfferDetailsResponse(
                jobOffer.getId(),
                jobOffer.getJobTitle(),
                jobOffer.getDescription(),
                jobOffer.getRequiredSkills(),
                jobOffer.getPreferredSkills(),
                jobOffer.getWorkLocations(),
                jobOffer.getTechnologies(),
                jobOffer.getCompany(),
                jobOffer.getTask().getStatus() != TaskStatus.NOT_YET_OPEN ? createTaskResponse(jobOffer.getTask(), userSolution, companyFeedback): null,
                jobOffer.getSalary(),
                jobOffer.getExperience(),
                jobOffer.getEmploymentType(),
                jobOffer.getTask().getOpensAt(),
                jobOffer.getTask().getClosesAt(),
                jobOffer.getTask().getStatus()
        );
    }

    private JobOfferDetailsResponse createJobOfferDetailsSolutionsResponse(JobOffer jobOffer) {
        return new JobOfferDetailsSolutionsResponse(
                jobOffer
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
                jobOffer.getSalary(),
                jobOffer.getTask().getStatus()
        );
    }

    public  UserTaskDetailsResponse createTaskResponse(Task task, Solution userSolution, Feedback companyFeedback) {
        return new UserTaskDetailsResponse(
                task.getId(),
                task.getTitle(),
                task.getInstructions(),
                task.getOpensAt(),
                task.getClosesAt(),
                task.getReward(),
                task.getAllowedLanguages(),
                task.getMemoryLimit(),
                task.getTimeLimit(),
                task.getStatus(),
                userSolution,
                companyFeedback
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
            Date minOpensIn,
            Date maxOpensIn,
            Sort sort
    ) {

        Range<Integer> salaryRange = Range.of(Range.Bound.inclusive(minSalary), Range.Bound.inclusive(maxSalary));
        Range<Integer> creditsRange = Range.of(Range.Bound.inclusive(minCredits), Range.Bound.inclusive(maxCredits));
        Range<Date> dateRange = Range.of(Range.Bound.inclusive(minOpensIn), Range.Bound.inclusive(maxOpensIn));

        PageRequest pageRequest = PageRequest.of(page, 3, sort);

        if (languages.isEmpty()) {
            return jobOfferRepository.findByJobTitleContainingIgnoreCaseAndSalaryBetweenAndTaskRewardBetweenAndTaskOpensAtBetween(q, salaryRange, creditsRange, dateRange, pageRequest)
                    .map(this::createJobOfferListResponse);
        } else {
            return jobOfferRepository.findByJobTitleContainingIgnoreCaseAndTaskAllowedLanguagesInIgnoreCaseAndSalaryBetweenAndTaskRewardBetweenAndTaskOpensAtBetween(q, languages, salaryRange, creditsRange, dateRange, pageRequest)
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


    public Feedback addFeedback(String solutionId, List<MultipartFile> files, Integer credits) {
        Solution solution = solutionRepository.findById(solutionId)
                .orElseThrow(() -> new RuntimeException("Solution not found for id " + solutionId));
        List<String> fileIDs = addFiles(files).stream().map(ObjectId::toString).toList();
        Feedback feedback = new Feedback(solution, fileIDs, credits);
        solution.setFeedback(feedback);
        solutionRepository.save(solution);
        //TODO feedback repsoitory i service ig, albo zostawicc tak juz idk, robione na szybko
        return feedback;
    };

    public List<DownloadFileResponse> downloadSolutionFilesForUser(String jobId, String userId) {
        List<DownloadFileResponse> downloadedFiles = new ArrayList<>();
        Optional<JobOffer> jobOfferOptional = jobOfferRepository.findById(jobId);
        if (jobOfferOptional.isPresent()) {
            JobOffer jobOffer = jobOfferOptional.get();
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            Optional<Solution> solution = jobOffer.getTask().getSolutionForUser(user);
            solution.ifPresent(value -> downloadedFiles.addAll(downloadSolutionFiles(value.getId())));
        }
        return downloadedFiles;
    }

    public List<DownloadFileResponse> downloadFeedbackFilesForUser(String jobId, String userId){
        JobOffer jobOffer = jobOfferRepository.findById(jobId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Optional<Feedback> feedback = jobOffer.getTask().getSolutionForUser(user).map(Solution::getFeedback);
        if(feedback.isPresent()){
            return downloadFiles(feedback.get().getFiles());
        }else{
            return Collections.emptyList();
        }
    }


    public List<DownloadFileResponse> downloadSolutionFiles(String solutionId) {
        Solution solution = solutionRepository.findById(solutionId).
                orElseThrow();

        return downloadFiles(solution.getFiles());
    }

    public List<DownloadFileResponse> downloadFiles(List<String> fileIDS) {
        List<DownloadFileResponse> downloadedFiles = new ArrayList<>();
        for (String fileId : fileIDS) {
            try {
                DownloadFile downloadFile = fileService.downloadFile(fileId);
                DownloadFileResponse downloadFileResponse = new DownloadFileResponse(downloadFile.getFilename(), downloadFile.getFileType(), downloadFile.getFile());
                downloadedFiles.add(downloadFileResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return downloadedFiles;
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

        existingSolution.getFiles().forEach((id) -> {
            try {
                fileService.DeleteFile(String.valueOf(id));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        List<ObjectId> fileIDs = addFiles(files);
        existingSolution.setFiles(fileIDs.stream().map(ObjectId::toString).toList());
        solutionRepository.save(existingSolution);
        System.out.println("Existing solution updated: " + existingSolution);
    }

    private void createNewSolution(Task task, List<MultipartFile> files, String userId){
        List<String> fileIDs = addFiles(files).stream().map(ObjectId::toString).toList();
        Solution solution = new Solution(task, getUser(userId), fileIDs);
        solutionRepository.save(solution);
        task.addSolution(solution);
        taskRepository.save(task);
    }

    private List<ObjectId> addFiles(List<MultipartFile> files) {
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
