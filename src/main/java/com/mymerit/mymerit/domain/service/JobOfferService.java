package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.response.DownloadFileResponse;
import com.mymerit.mymerit.api.payload.response.JobOfferDetailsResponse;
import com.mymerit.mymerit.api.payload.response.JobOfferListResponse;
import com.mymerit.mymerit.api.payload.response.UserTaskDetailsResponse;
import com.mymerit.mymerit.domain.entity.*;
import com.mymerit.mymerit.domain.models.TaskStatus;
import com.mymerit.mymerit.infrastructure.repository.JobOfferRepository;
import com.mymerit.mymerit.infrastructure.repository.SolutionRepository;
import com.mymerit.mymerit.infrastructure.repository.TaskRepository;
import com.mymerit.mymerit.infrastructure.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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

    public Optional<JobOfferDetailsResponse> getJobOfferDetailsResponse(String id, UserDetailsImpl userDetails) {
        Optional<User> userOptional = userRepository.findById(userDetails.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println(jobOfferRepository.findById(id).get().getTask().getSolutionForUser(user));
            return jobOfferRepository.findById(id)
                    .map(jobOffer -> createJobOfferDetailsResponse(jobOffer, jobOffer.getTask().getSolutionForUser(user)));
        } else {
            throw new RuntimeException("User not found with id: " + userDetails.getId());
        }
    }

    private JobOfferDetailsResponse createJobOfferDetailsResponse(JobOffer jobOffer, Solution userSolution) {
        return new JobOfferDetailsResponse(
                jobOffer.getId(),
                jobOffer.getJobTitle(),
                jobOffer.getDescription(),
                jobOffer.getRequiredSkills(),
                jobOffer.getPreferredSkills(),
                jobOffer.getWorkLocations(),
                jobOffer.getTechnologies(),
                jobOffer.getCompany(),
                jobOffer.getTask().getStatus() != TaskStatus.NOT_YET_OPEN ? createTaskResponse(jobOffer.getTask(), userSolution): null,
                jobOffer.getSalary(),
                jobOffer.getExperience(),
                jobOffer.getMode(),
                jobOffer.getTask().getOpensAt(),
                jobOffer.getTask().getClosesAt(),
                jobOffer.getTask().getStatus()
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

    private UserTaskDetailsResponse createTaskResponse(Task task, Solution userSolution){
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
                userSolution
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

    public List<DownloadFileResponse> downloadSolutionFilesForUser(String jobId, String userId) {
        List<DownloadFileResponse> downloadedFiles = new ArrayList<>();
        Optional<JobOffer> jobOfferOptional = jobOfferRepository.findById(jobId);
        if (jobOfferOptional.isPresent()) {
            JobOffer jobOffer = jobOfferOptional.get();
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            Solution solution = jobOffer.getTask().getSolutionForUser(user);
            if (solution != null) {
                for (String fileId : solution.getFiles()) {
                    try {
                        DownloadFile downloadFile = fileService.downloadFile(fileId);
                        DownloadFileResponse downloadFileResponse = new DownloadFileResponse(downloadFile.getFilename(), downloadFile.getFileType(), downloadFile.getFile());
                        downloadedFiles.add(downloadFileResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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

        existingSolution.files.forEach((id) -> {
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
