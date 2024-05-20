package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.response.*;
import com.mymerit.mymerit.domain.entity.*;
import com.mymerit.mymerit.domain.service.JobOfferService;
import com.mymerit.mymerit.domain.service.MailSenderService;
import com.mymerit.mymerit.domain.service.UserDetailsImpl;
import com.mymerit.mymerit.infrastructure.repository.*;
import com.mymerit.mymerit.infrastructure.security.CurrentUser;
import com.mymerit.mymerit.api.payload.request.UpdateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Tag(name = "UserController")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SocialRepository socialRepository;
    private final SolutionRepository solutionRepository;

    private final TaskHistoryRepository taskHistoryRepository;
    private final JobOfferHistoryRepository jobOfferHistoryRepository;
    private final JobOfferRepository jobOfferRepository;
    private final BookmarkRepository bookmarkRepository;


    private final RewardHistoryRepository rewardHistoryRepository;
    private final RewardRepository rewardRepository;
    private final MailSenderService mailSenderService;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, SocialRepository socialRepository,
                          TaskHistoryRepository taskHistoryRepository, JobOfferHistoryRepository jobOfferHistoryRepository,
                          RewardHistoryRepository rewardHistoryRepository, RewardRepository rewardRepository, MailSenderService mailSenderService, SolutionRepository solutionRepository, JobOfferRepository jobOfferRepository, BookmarkRepository bookmarkRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.socialRepository = socialRepository;
        this.taskHistoryRepository = taskHistoryRepository;
        this.jobOfferHistoryRepository = jobOfferHistoryRepository;
        this.rewardHistoryRepository = rewardHistoryRepository;
        this.rewardRepository = rewardRepository;
        this.mailSenderService = mailSenderService;
        this.solutionRepository = solutionRepository;
        this.jobOfferRepository = jobOfferRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    @Operation(summary = "get current user")


    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserDetailsImpl userDetailsImpl) {
        return userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " not found"));
    }

    @Operation(summary = "purchase a reward")


    @PostMapping("/me/purchase/{idReward}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Reward> purchaseReward(@CurrentUser UserDetailsImpl userDetailsImpl, @PathVariable String idReward) {
        User user = userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " not found"));

        Optional<Reward> reward = rewardRepository.findById(idReward);

        if (reward.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (user.getCredits() < reward.get().getCost()) {
            return ResponseEntity.badRequest().build();
        }

        user.setCredits(user.getCredits() - reward.get().getCost());
        userRepository.save(user);

        RewardHistory rewardHistory = new RewardHistory();
        rewardHistory.setUser(user);
        rewardHistory.setReward(reward.get());
        rewardHistory.setDatePurchase(LocalDateTime.now());
        rewardHistoryRepository.insert(rewardHistory);

        mailSenderService.sendReward(reward.get().getName(), user.getEmail());

        return ResponseEntity.ok(reward.get());
    }

    @Operation( summary = "get current user reward history")
    @GetMapping("/me/rewards")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RewardHistoryResponse>> getCurrentUserRewards(@CurrentUser UserDetailsImpl userDetailsImpl) {
        User user = userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " not found"));

        List<RewardHistory> userRewards = rewardHistoryRepository.findByUser(user)
                .orElse(new ArrayList<>());

        List<RewardHistoryResponse> rewardHistoryResponse = new ArrayList<>();

        for (RewardHistory rewardHistory : userRewards) {
            rewardHistoryResponse.add(new RewardHistoryResponse(rewardHistory.getReward(), rewardHistory.getDatePurchase()));
        }

        return ResponseEntity.ok(rewardHistoryResponse);
    }

    @Operation(summary = "Lists all users")
    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @Operation(summary = "Updates user data")
    @PostMapping("/me/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUserProfileInfo(@CurrentUser UserDetailsImpl userDetailsImpl, @Valid @RequestBody UpdateUserRequest updateUserRequest){
        Optional<User> user_check = userRepository.findById(userDetailsImpl.getId());
        User user;
        if( user_check != null){
            user = user_check.get();
        }
        else{
            return ResponseEntity.badRequest().body(new ApiResponse(false, "user doesnt exist"));
        }
        boolean changed = false;

        if( updateUserRequest.getImageBase64() != null ){
            user.setImageBase64(updateUserRequest.getImageBase64());
            changed = true;
        }
        if( updateUserRequest.getDescription() != null ){
            user.setDescription(updateUserRequest.getDescription());
            changed = true;
        }
        if( updateUserRequest.getPassword() != null ){
            user.setPassword(updateUserRequest.getPassword());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            changed = true;
        }
        if( updateUserRequest.getUsername() != null ){
            user.setUsername(updateUserRequest.getUsername());
            changed = true;
        }
        if( changed == true ){
            userRepository.save(user);
            return ResponseEntity.ok( ).body(new ApiResponse(true, "account data updated"));
        }
        return ResponseEntity.badRequest().body(new ApiResponse(false, "failed to update account data"));
    }

    @Operation(  summary = "Lists user socials")
    @GetMapping("/me/socials")
    @PreAuthorize("hasRole('USER')")
    public Socials getUserSocials(@CurrentUser UserDetailsImpl userDetailsImpl){
        return socialRepository.findByUserId(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " social media not found"));

    }

    @Operation(summary = "Lists users task history")
    @GetMapping("/me/mytasks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TaskHistoryResponse>> getCurrentUserTaskHistory(@CurrentUser UserDetailsImpl userDetailsImpl) {
        User user = userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " not found"));

        List<TaskHistory> userTasks = taskHistoryRepository.findByUserId(user.getId())
                .orElse(new ArrayList<>());

        List<TaskHistoryResponse> taskHistoryResponse = new ArrayList<>();

        for (TaskHistory taskHistory : userTasks) {
            taskHistoryResponse.add(new TaskHistoryResponse(taskHistory.getTask(), taskHistory.getDateLastModified()));
        }

        return ResponseEntity.ok(taskHistoryResponse);
    }

    @Operation( summary = "Lists users solutions")
    @GetMapping("/me/solutions")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<SolutionListResponse>> getCurrentUserSolutionHistory(@CurrentUser UserDetailsImpl userDetailsImpl) {
        User user = userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " not found"));

        List<SolutionListResponse> solutionResponses = solutionRepository.findAllByUserId(user.getId())
                .stream()
                .map(solution -> {
                    Task task = solution.getTask();
                    JobOffer job = (task != null) ? task.getJob() : null;

                    return new SolutionListResponse(
                            (job != null) ? job.getId() : null,
                            (task != null) ? task.getTitle() : "Deleted Task",
                            solution.getSubmitDate(),
                            solution.getFeedback(),
                            solution.getLanguage(),
                            (job != null) ? jobOfferRepository.findById(job.getId())
                                    .map(JobOffer::getCompany)
                                    .map(User::getImageBase64)
                                    .orElse("") : ""
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(solutionResponses);
    }
    @Operation( summary = "Lists users bookmarks")
    @GetMapping("/me/bookmarks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<JobOfferListResponse>> getCurrentUserBookmarks(@CurrentUser UserDetailsImpl userDetailsImpl) {
        User user = userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " not found"));

        List<JobOfferListResponse> bookmarkedJobOffers = bookmarkRepository.findByUserId(user.getId())
                .stream()
                .map(Bookmark::getJobOffer)
                .map(JobOfferService::createJobOfferListResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(bookmarkedJobOffers);
    }

    @Operation( summary = "Gets company user")
    @GetMapping("/company")
    @PreAuthorize("hasRole('COMPANY')")
    public User getCurrentCompanyUser(@CurrentUser UserDetailsImpl userDetailsImpl) {
        return userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("Company user " + userDetailsImpl.getId() + " not found"));
    }

    @Operation( summary = "Updates users data")
    @PostMapping("/company/update")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<?> updateCompanyUserProfileInfo(@CurrentUser UserDetailsImpl userDetailsImpl, @Valid @RequestBody UpdateUserRequest updateUserRequest){
        Optional<User> user_check = userRepository.findById(userDetailsImpl.getId());
        User user;
        if( !user_check.isPresent()){
            user = user_check.get();
        }
        else{
            return ResponseEntity.badRequest().body(new ApiResponse(false, "company user doesnt exist"));
        }
        boolean changed = false;

        if( updateUserRequest.getImageBase64() != null ){
            user.setImageBase64(updateUserRequest.getImageBase64());
            changed = true;
        }
        if( updateUserRequest.getDescription() != null ){
            user.setDescription(updateUserRequest.getDescription());
            changed = true;
        }
        if( updateUserRequest.getPassword() != null ){
            user.setPassword(updateUserRequest.getPassword());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            changed = true;
        }
        if( updateUserRequest.getUsername() != null ){
            user.setUsername(updateUserRequest.getUsername());
            changed = true;
        }
        if( changed == true ){
            userRepository.save(user);
            return ResponseEntity.ok( ).body(new ApiResponse(true, "account data updated"));
        }
        return ResponseEntity.badRequest().body(new ApiResponse(false, "failed to update account data"));
    }

    @Operation(  summary = "Lists company's socials")
    @GetMapping("/company/socials")
    @PreAuthorize("hasRole('COMPANY')")
    public Socials getCompanyUserSocials(@CurrentUser UserDetailsImpl userDetailsImpl){
        return socialRepository.findByUserId(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("Company user " + userDetailsImpl.getId() + " social media not found"));

    }
    @Operation( summary = "Lists company's job offer history")
    @GetMapping("/company/myjoboffers")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<JobOfferHistoryResponse>> getCurrentCompanyUserJobOfferHistory(@CurrentUser UserDetailsImpl userDetailsImpl) {
        User user = userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " not found"));

        List<JobOfferHistory> userJobOffers = jobOfferHistoryRepository.findByUserId(user.getId())
                .orElse(new ArrayList<>());

        if (userJobOffers.isEmpty() ) {
            return ResponseEntity.notFound().build();
        }

        List<JobOfferHistoryResponse> jobOfferHistoryResponse = new ArrayList<>();

        for (JobOfferHistory jobOfferHistory : userJobOffers) {
            jobOfferHistoryResponse.add(new JobOfferHistoryResponse(jobOfferHistory.getJobOffer(), jobOfferHistory.getDateLastModified()));
        }

        return ResponseEntity.ok(jobOfferHistoryResponse);
    }

}