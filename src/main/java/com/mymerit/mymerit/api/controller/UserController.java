package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.response.RewardHistoryResponse;
import com.mymerit.mymerit.domain.entity.Reward;
import com.mymerit.mymerit.domain.entity.RewardHistory;
import com.mymerit.mymerit.domain.entity.User;
import com.mymerit.mymerit.domain.service.UserDetailsImpl;
import com.mymerit.mymerit.infrastructure.repository.RewardHistoryRepository;
import com.mymerit.mymerit.infrastructure.repository.RewardRepository;
import com.mymerit.mymerit.infrastructure.repository.UserRepository;
import com.mymerit.mymerit.infrastructure.security.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final RewardHistoryRepository rewardHistoryRepository;
    private final RewardRepository rewardRepository;

    public UserController(UserRepository userRepository, RewardHistoryRepository rewardHistoryRepository, RewardRepository rewardRepository) {
        this.userRepository = userRepository;
        this.rewardHistoryRepository = rewardHistoryRepository;
        this.rewardRepository = rewardRepository;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserDetailsImpl userDetailsImpl) {
        return userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " not found"));
    }

    @PostMapping("/me/purchase/{idReward}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Reward> purchaseReward(@CurrentUser UserDetailsImpl userDetailsImpl, @PathVariable String idReward) {
        User user = userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " not found"));

        Optional<Reward> reward = rewardRepository.findById(idReward);

        if (reward.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (user.getPoints() < reward.get().getCost()) {
            return ResponseEntity.badRequest().build();
        }

        user.setPoints(user.getPoints() - reward.get().getCost());
        userRepository.save(user);

        RewardHistory rewardHistory = new RewardHistory();
        rewardHistory.setUser(user);
        rewardHistory.setReward(reward.get());
        rewardHistory.setDatePurchase(LocalDateTime.now());
        rewardHistoryRepository.insert(rewardHistory);

        return ResponseEntity.ok(reward.get());
    }

    @GetMapping("/me/rewards")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RewardHistoryResponse>> getCurrentUserRewards(@CurrentUser UserDetailsImpl userDetailsImpl) {
        User user = userRepository.findById(userDetailsImpl.getId())
                .orElseThrow(() -> new RuntimeException("User " + userDetailsImpl.getId() + " not found"));

        List<RewardHistory> userRewards = rewardHistoryRepository.findByUser(user)
                .orElse(new ArrayList<>());

        if (userRewards.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<RewardHistoryResponse> rewardHistoryResponse = new ArrayList<>();

        for (RewardHistory rewardHistory : userRewards) {
            rewardHistoryResponse.add(new RewardHistoryResponse(rewardHistory.getReward(), rewardHistory.getDatePurchase()));
        }

        return ResponseEntity.ok(rewardHistoryResponse);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}