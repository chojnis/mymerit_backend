package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.request.UpdateUserRequest;
import com.mymerit.mymerit.api.payload.response.*;
import com.mymerit.mymerit.domain.entity.*;
import com.mymerit.mymerit.domain.exception.InsufficientCreditsException;
import com.mymerit.mymerit.domain.exception.RewardNotFoundException;
import com.mymerit.mymerit.domain.exception.UserNotFoundException;
import com.mymerit.mymerit.domain.service.JobOfferService;
import com.mymerit.mymerit.domain.service.RewardService;
import com.mymerit.mymerit.domain.service.UserDetailsImpl;
import com.mymerit.mymerit.infrastructure.repository.*;
import com.mymerit.mymerit.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Tag(name = "UserController")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SolutionRepository solutionRepository;

    private final TaskHistoryRepository taskHistoryRepository;
    private final JobOfferHistoryRepository jobOfferHistoryRepository;
    private final JobOfferRepository jobOfferRepository;
    private final BookmarkRepository bookmarkRepository;

    private final RewardService rewardService;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          TaskHistoryRepository taskHistoryRepository, JobOfferHistoryRepository jobOfferHistoryRepository,
                          SolutionRepository solutionRepository, JobOfferRepository jobOfferRepository,
                          BookmarkRepository bookmarkRepository, RewardService rewardService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.taskHistoryRepository = taskHistoryRepository;
        this.jobOfferHistoryRepository = jobOfferHistoryRepository;
        this.solutionRepository = solutionRepository;
        this.jobOfferRepository = jobOfferRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.rewardService = rewardService;
    }

    @Operation(summary = "Find user by their ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOptional.get();


        UserResponse userResponse = new UserResponse(user);

        return ResponseEntity.ok(userResponse);
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
        try {
            Reward reward = rewardService.purchaseReward(userDetailsImpl.getId(), idReward);

            return ResponseEntity.ok(reward);
        } catch (UserNotFoundException | RewardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InsufficientCreditsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "get current user reward history")
    @GetMapping("/me/rewards")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RewardHistoryResponse>> getCurrentUserRewards(@CurrentUser UserDetailsImpl userDetailsImpl) {
        try {
            List<RewardHistoryResponse> rewardHistoryResponse = rewardService.getCurrentUserRewardHistory(userDetailsImpl.getId());

            return ResponseEntity.ok(rewardHistoryResponse);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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
    public ResponseEntity<ApiResponse> updateUserProfileInfo(@CurrentUser UserDetailsImpl userDetailsImpl, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return updateUser(userDetailsImpl.getId(), updateUserRequest);
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

    @Operation(summary = "Lists users solutions")
    @GetMapping("/me/solutions")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<SolutionListResponse>> getCurrentUserSolutionHistory(@CurrentUser UserDetailsImpl userDetailsImpl) {
        User user = userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " not found"));

        List<SolutionListResponse> solutionResponses = solutionRepository.findAllByUserId(user.getId())
                .stream()
                .sorted(Comparator.comparing(Solution::getSubmitDate).reversed())
                .map(solution -> {
                    boolean isFeedbackToday = solution.getFeedback() != null && solution.getFeedback().getSubmitDate().toLocalDate().isEqual(LocalDate.now());

                    return new SolutionListResponse(
                            solution.getTask() != null ? solution.getTask().getJob().getId() : null,
                            solution.getTask() != null ? solution.getTask().getTitle() : null,
                            solution.getSubmitDate(),
                            solution.getFeedback(),
                            solution.getLanguage(),
                            solution.getTask() != null ? jobOfferRepository.findById(solution.getTask().getJob().getId())
                                    .map(JobOffer::getCompany)
                                    .map(User::getImageBase64)
                                    .orElse("") : "",
                            isFeedbackToday
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(solutionResponses);
    }

    @Operation(summary = "Lists users bookmarks")
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

    @Operation(summary = "Gets company user")
    @GetMapping("/company")
    @PreAuthorize("hasRole('COMPANY')")
    public User getCurrentCompanyUser(@CurrentUser UserDetailsImpl userDetailsImpl) {
        return userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("Company user " + userDetailsImpl.getId() + " not found"));
    }

    @Operation(summary = "Updates users data")
    @PostMapping("/company/update")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<ApiResponse> updateCompanyUserProfileInfo(@CurrentUser UserDetailsImpl userDetailsImpl, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return updateUser(userDetailsImpl.getId(), updateUserRequest);
    }

    @Operation(summary = "Lists company's job offer history")
    @GetMapping("/company/myjoboffers")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<JobOfferHistoryResponse>> getCurrentCompanyUserJobOfferHistory(@CurrentUser UserDetailsImpl userDetailsImpl) {
        User user = userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " not found"));

        List<JobOfferHistory> userJobOffers = jobOfferHistoryRepository.findByUserId(user.getId())
                .orElse(new ArrayList<>());

        if (userJobOffers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<JobOfferHistoryResponse> jobOfferHistoryResponse = new ArrayList<>();

        for (JobOfferHistory jobOfferHistory : userJobOffers) {
            jobOfferHistoryResponse.add(new JobOfferHistoryResponse(jobOfferHistory.getJobOffer(), jobOfferHistory.getDateLastModified()));
        }

        return ResponseEntity.ok(jobOfferHistoryResponse);
    }

    private boolean updateUserFields(User user, UpdateUserRequest updateUserRequest) {
        boolean changed = false;

        if (updateUserRequest.getImageBase64() != null) {
            user.setImageBase64(updateUserRequest.getImageBase64());
            changed = true;
        }

        if (updateUserRequest.getDescription() != null) {
            user.setDescription(updateUserRequest.getDescription());
            changed = true;
        }

        if (updateUserRequest.getPassword() != null) {
            user.setPassword(updateUserRequest.getPassword());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            changed = true;
        }

        if (updateUserRequest.getUsername() != null) {
            user.setUsername(updateUserRequest.getUsername());
            changed = true;
        }

        if (updateUserRequest.getSocialLink1() != null) {
            user.setSocialLink1(updateUserRequest.getSocialLink1());
            changed = true;
        }

        if (updateUserRequest.getSocialName1() != null) {
            user.setSocialName1(updateUserRequest.getSocialName1());
            changed = true;
        }

        if (updateUserRequest.getSocialLink2() != null) {
            user.setSocialLink2(updateUserRequest.getSocialLink2());
            changed = true;
        }

        if (updateUserRequest.getSocialName2() != null) {
            user.setSocialName2(updateUserRequest.getSocialName2());
            changed = true;
        }

        if (updateUserRequest.getSocialLink3() != null) {
            user.setSocialLink3(updateUserRequest.getSocialLink3());
            changed = true;
        }

        if (updateUserRequest.getSocialName3() != null) {
            user.setSocialName3(updateUserRequest.getSocialName3());
            changed = true;
        }

        return changed;
    }

    private ResponseEntity<ApiResponse> updateUser(String userId, UpdateUserRequest updateUserRequest) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "user doesn't exist"));
        }

        User user = userOptional.get();
        boolean changed = updateUserFields(user, updateUserRequest);

        if (changed) {
            userRepository.save(user);

            return ResponseEntity.ok().body(new ApiResponse(true, "account data updated"));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "failed to update account data"));
        }
    }
}
