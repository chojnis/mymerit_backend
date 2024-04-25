package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.domain.entity.User;
import com.mymerit.mymerit.domain.entity.Socials;
import com.mymerit.mymerit.domain.entity.Task;
import com.mymerit.mymerit.domain.entity.TaskHistory;
import com.mymerit.mymerit.domain.service.UserDetailsImpl;
import com.mymerit.mymerit.infrastructure.repository.UserRepository;
import com.mymerit.mymerit.infrastructure.repository.SocialRepository;
import com.mymerit.mymerit.infrastructure.repository.TaskHistoryRepository;
import com.mymerit.mymerit.infrastructure.security.CurrentUser;
import com.mymerit.mymerit.api.payload.response.ApiResponse;
import com.mymerit.mymerit.api.payload.response.TaskHistoryResponse;
import com.mymerit.mymerit.api.payload.request.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SocialRepository socialRepository;
    private final TaskHistoryRepository taskHistoryRepository;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, SocialRepository socialRepository, TaskHistoryRepository taskHistoryRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.socialRepository = socialRepository;
        this.taskHistoryRepository = taskHistoryRepository;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserDetailsImpl userDetailsImpl) {
        return userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " not found"));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

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
        Boolean changed = false;
        
        if( updateUserRequest.getImageUrl() != null ){
                user.setImageUrl(updateUserRequest.getImageUrl());
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

    @GetMapping("/me/socials")
    @PreAuthorize("hasRole('USER')")
    public Socials getUserSocials(@CurrentUser UserDetailsImpl userDetailsImpl){
        return socialRepository.findByUserId(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " social media not found"));

    }

    @GetMapping("/me/mytasks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TaskHistoryResponse>> getCurrentUserTaskHistory(@CurrentUser UserDetailsImpl userDetailsImpl) {
        User user = userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " not found"));

        List<TaskHistory> userTasks = taskHistoryRepository.findByUserId(user.getId())
                .orElse(new ArrayList<>());

        if (userTasks.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<TaskHistoryResponse> taskHistoryResponse = new ArrayList<>();

        for (TaskHistory taskHistory : userTasks) {
            taskHistoryResponse.add(new TaskHistoryResponse(taskHistory.getTask(), taskHistory.getDateLastModified()));
        }

        return ResponseEntity.ok(taskHistoryResponse);
    }

    @GetMapping("/company")
    @PreAuthorize("hasRole('COMPANY')")
    public User getCurrentCompanyUser(@CurrentUser UserDetailsImpl userDetailsImpl) {
        return userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("Company user " + userDetailsImpl.getId() + " not found"));
    }

    @PostMapping("/company/update")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<?> updateCompanyUserProfileInfo(@CurrentUser UserDetailsImpl userDetailsImpl, @Valid @RequestBody UpdateUserRequest updateUserRequest){
        Optional<User> user_check = userRepository.findById(userDetailsImpl.getId());
        User user;
        if( user_check != null){
            user = user_check.get();
        }
        else{
            return ResponseEntity.badRequest().body(new ApiResponse(false, "company user doesnt exist"));
        }
        Boolean changed = false;
        
        if( updateUserRequest.getImageUrl() != null ){
                user.setImageUrl(updateUserRequest.getImageUrl());
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

    @GetMapping("/company/socials")
    @PreAuthorize("hasRole('COMPANY')")
    public Socials getCompanyUserSocials(@CurrentUser UserDetailsImpl userDetailsImpl){
        return socialRepository.findByUserId(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("Company user " + userDetailsImpl.getId() + " social media not found"));

    }

}