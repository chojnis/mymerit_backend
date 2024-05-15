package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.request.JobOfferRequest;
import com.mymerit.mymerit.api.payload.request.JudgeTokenRequest;
import com.mymerit.mymerit.api.payload.response.*;
import com.mymerit.mymerit.domain.entity.*;
import com.mymerit.mymerit.domain.models.ProgrammingLanguage;
import com.mymerit.mymerit.domain.models.TaskStatus;
import com.mymerit.mymerit.infrastructure.repository.*;
import org.bson.types.ObjectId;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class JobOfferService {
    JobOfferRepository jobOfferRepository;
    JudgeService judgeService;
    TaskTestService taskTestService;
    UserRepository userRepository;
    GridFileService fileService;
    TaskRepository taskRepository;
    TaskHistoryRepository taskHistoryRepository;
    JobOfferHistoryRepository jobOfferHistoryRepository;
    SolutionRepository solutionRepository; //nah co tu sie dzieje xd troche duzo tego
    FeedbackRepository feedbackRepository;
    GridFileService gridFileService;
    BookmarkRepository bookmarkRepository;

    JobOfferService(
            GridFileService gridFileService,
            JudgeService judgeService,
            TaskTestService taskTestService,
            JobOfferRepository jobOfferRepository,
            UserRepository userRepository,
            GridFileService fileService,
            TaskRepository taskRepository,
            SolutionRepository solutionRepository,
            TaskHistoryRepository taskHistoryRepository,
            JobOfferHistoryRepository jobOfferHistoryRepository,
            FeedbackRepository feedbackRepository,
            BookmarkRepository bookmarkRepository
    ) {
        this.taskTestService = taskTestService;
        this.gridFileService = gridFileService;
        this.judgeService = judgeService;
        this.jobOfferRepository = jobOfferRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.taskRepository = taskRepository;
        this.solutionRepository = solutionRepository;
        this.taskHistoryRepository = taskHistoryRepository;
        this.jobOfferHistoryRepository = jobOfferHistoryRepository;
        this.feedbackRepository = feedbackRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    public JobOffer addJobOffer(JobOfferRequest jobOfferRequest, UserDetailsImpl userDetails){
        User user = getUser(userDetails.getId());

        Integer userCredits = user.getCredits();
        Integer taskReward = jobOfferRequest.getTask().getReward();

        if (userCredits < taskReward) {
            throw new RuntimeException("Not enough credits to create job offer");
        }

        user.setCredits(userCredits - taskReward);
        Task createdTask = taskRepository.save(jobOfferRequest.getTask());

        JobOffer jobOffer = new JobOffer(
                jobOfferRequest.getJobTitle(),
                jobOfferRequest.getDescription(),
                jobOfferRequest.getRequiredSkills(),
                jobOfferRequest.getPreferredSkills(),
                jobOfferRequest.getWorkLocations(),
                jobOfferRequest.getTechnologies(),
                user,
                createdTask,
                jobOfferRequest.getExperience(),
                jobOfferRequest.getEmploymentType(),
                jobOfferRequest.getSalary()
        );
        JobOffer createdJobOffer = jobOfferRepository.save(jobOffer);
        createdJobOffer.getTask().setJob(createdJobOffer);
        jobOfferRepository.save(jobOffer);
        createdTask.setJob(createdJobOffer);
        taskRepository.save(createdTask);
        JobOfferHistory jobOfferHistory = new JobOfferHistory(createdJobOffer, jobOffer.getCompany().getId(), LocalDateTime.now());
        jobOfferHistoryRepository.save(jobOfferHistory);

        return createdJobOffer;
    }

    public Task getTaskForJobOffer(String jobOfferId){
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId).orElseThrow(()->new RuntimeException("Job offer not found"));
        return taskRepository.findById(jobOffer.getTask().getId()).orElseThrow(()->new RuntimeException("Task not found"));
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

        return createJobOfferDetailsResponse(jobOffer, userSolution.orElse(null), userSolution.map(Solution::getFeedback).orElse(null), bookmarkRepository.findByUserIdAndJobOfferId(userDetails.getId(),id).isPresent());

    }


    private JobOfferDetailsResponse createJobOfferDetailsResponse(JobOffer jobOffer, Solution userSolution, Feedback companyFeedback, Boolean isBookmarked) {
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
                jobOffer.getTask().getStatus(),
                isBookmarked
        );
    }

    private JobOfferDetailsResponse createJobOfferDetailsSolutionsResponse(JobOffer jobOffer) {
        return new JobOfferDetailsSolutionsResponse(
                jobOffer
        );
    }

    public static JobOfferListResponse createJobOfferListResponse(JobOffer jobOffer){
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
                companyFeedback,
                task.getTemplateFiles(),
                task.getTests()
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
            return jobOfferRepository.findByJobTitleContainingIgnoreCaseAndSalaryBetweenAndTaskRewardBetweenAndTaskClosesAtBetween(q, salaryRange, creditsRange, dateRange, pageRequest)
                    .map(JobOfferService::createJobOfferListResponse);
        } else {
            return jobOfferRepository.findByJobTitleContainingIgnoreCaseAndTaskAllowedLanguagesInIgnoreCaseAndSalaryBetweenAndTaskRewardBetweenAndTaskClosesAtBetween(q, languages, salaryRange, creditsRange, dateRange, pageRequest)
                    .map(JobOfferService::createJobOfferListResponse);
        }
    }


    public JobOffer addSolution(String jobOfferId, List<MultipartFile> files, String userId,ProgrammingLanguage language) throws IOException {
        JobOffer jobOffer = getJobOfferOrThrow(jobOfferId);
        Task task = jobOffer.getTask();


        if (userAlreadySubmittedSolution(task, userId)) {
            updateExistingSolution(task, files, userId, language);
        } else {
            createNewSolution(task, files, userId, language);
        }

        executeTests(userId,task,language,files);

        return jobOfferRepository.save(jobOffer);
    }

    public void executeTests(String userId, Task task, ProgrammingLanguage language, List<MultipartFile> files) throws IOException {
       Solution solution =  task.findSolutionByUserId(userId);

        Optional<String> optionalTestFileBase64 = task.getTestByLanguage(language)
                .map(CodeTest::getTestFileBase64);

        if (optionalTestFileBase64.isPresent()) {
            solution.setTestResults(taskTestService.executeAllTests(files, task.getId(), language));
        }

       solutionRepository.save(solution);
    }



    private MultipartFile convertBase64ToMultipartFile(String fileName, String base64Data) {
        byte[] fileContent = Base64.getDecoder().decode(base64Data);
        return new MockMultipartFile(//narazie tak, pewnie zmienie
                "file",
                fileName,
                "text/plain",
                fileContent
        );
    }

    public Feedback addFeedback(String solutionId, List<MultipartFile> files, Integer credits, String comment, String companyId) {
        Solution solution = solutionRepository.findById(solutionId)
                .orElseThrow(() -> new RuntimeException("Solution not found for id " + solutionId));
        User company = userRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("User not found for id " + solutionId));
        List<String> fileIDs = addFiles(files).stream().map(ObjectId::toString).toList();
        if(company.getCredits() < credits){
            throw new RuntimeException("Not enought credits");
        }
        Feedback feedback = new Feedback(solution, fileIDs, credits, comment);
        User user = solution.getUser();
        user.setCredits(user.getCredits() + credits);
        feedbackRepository.save(feedback);
        solution.setFeedback(feedback);
        solutionRepository.save(solution);
        userRepository.save(user);
        return feedback;
    };

    public List<GridFileResponse> downloadSolutionFilesForUser(String jobId, String userId) {
        List<GridFileResponse> downloadedFiles = new ArrayList<>();
        Optional<JobOffer> jobOfferOptional = jobOfferRepository.findById(jobId);
        if (jobOfferOptional.isPresent()) {
            JobOffer jobOffer = jobOfferOptional.get();
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            Optional<Solution> solution = jobOffer.getTask().getSolutionForUser(user);
            solution.ifPresent(value -> downloadedFiles.addAll(downloadSolutionFiles(value.getId()).getFiles()));
        }
        return downloadedFiles;
    }

    public List<GridFileResponse> downloadFeedbackFilesForUser(String jobId, String userId){
        JobOffer jobOffer = jobOfferRepository.findById(jobId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Optional<Feedback> feedback = jobOffer.getTask().getSolutionForUser(user).map(Solution::getFeedback);
        if(feedback.isPresent()){
            return gridFileService.downloadFiles(feedback.get().getFiles());
        }else{
            return Collections.emptyList();
        }
    }

    public List<GridFileResponse> downloadFeedbackForSolution(String solutionId){
        Solution solution = solutionRepository.findById(solutionId).orElseThrow();
        /*if(!userId.equals(solution.getUser().getId())){
            throw new RuntimeException("Unauthorized");
        }*/
        Feedback feedback = solution.getFeedback();
        if(feedback != null){
            return gridFileService.downloadFiles(feedback.getFiles());
        }else{
            return Collections.emptyList();
        }
    }

    public SolutionDetailsResponse downloadSolutionFiles(String solutionId) {
        Solution solution = solutionRepository.findById(solutionId).
                orElseThrow();
        return  new SolutionDetailsResponse(solution.getUser(), solution.getLanguage(), solution.getTestResults(), gridFileService.downloadFiles(solution.getFiles()), solution.getFeedback() != null);

    }


    private JobOffer getJobOfferOrThrow(String jobOfferId) {
        return jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> new IllegalArgumentException("Job offer not found for id: " + jobOfferId));
    }

    private boolean userAlreadySubmittedSolution(Task task, String userId) {
        return task.getSolutions().stream()
                .anyMatch(solution -> solution.getUser().getId().equals(userId));
    }

    private void updateExistingSolution(Task task, List<MultipartFile> files, String userId, ProgrammingLanguage language){
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

        List<String> fileIDs = addFiles(files).stream().map(ObjectId::toString).toList();
        existingSolution.setFiles(fileIDs);
        existingSolution.setLanguage(language);
        //existingSolution.setMainFileId(fileIDs.get(findFileIndexByName(files, mainFileName)));
        solutionRepository.save(existingSolution);
        System.out.println("Existing solution updated: " + existingSolution);

        Optional<List<TaskHistory>> taskHistoryList = taskHistoryRepository.findByUserId(userId);
        if(taskHistoryList.isEmpty()){
            throw new IllegalStateException("Task history record not found for the user");
        }
        TaskHistory taskHistory = taskHistoryList.get()
                .stream()
                .filter(taskhist -> taskhist.getTask().getId().equals(task.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Solution not found for the user"));

        taskHistory.setDateLastModified(LocalDateTime.now());
        taskHistoryRepository.save(taskHistory);
    }

    private void createNewSolution(Task task, List<MultipartFile> files, String userId, ProgrammingLanguage language){
        List<String> fileIDs = addFiles(files).stream().map(ObjectId::toString).toList();
        Solution solution = new Solution(task, getUser(userId), fileIDs);

        //solution.setMainFileId(fileIDs.get(findFileIndexByName(files, mainFileName)));
        solution.setLanguage(language);

        solutionRepository.save(solution);
        task.addSolution(solution);
        taskRepository.save(task);
        TaskHistory taskHistory = new TaskHistory(task, userId, LocalDateTime.now());
        taskHistoryRepository.save(taskHistory);
    }

    public int findFileIndexByName(List<MultipartFile> files, String filename) {
        return files.stream()
                .filter(file -> file.getOriginalFilename().equals(filename))
                .findFirst()
                .map(files::indexOf)
                .orElseThrow(() -> new RuntimeException("Failed to find main file"));
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
