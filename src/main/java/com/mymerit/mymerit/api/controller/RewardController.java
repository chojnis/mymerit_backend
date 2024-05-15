package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.domain.entity.Reward;
import com.mymerit.mymerit.infrastructure.repository.RewardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class RewardController {
    private final RewardRepository rewardRepository;

    public RewardController(RewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }

    @GetMapping("/rewards")
    public ResponseEntity<List<Reward>> getRewards() {
        List<Reward> rewards = rewardRepository.findAll();

        return ResponseEntity.ok(rewards);
    }

    @GetMapping("/rewards/{id}")
    public ResponseEntity<Reward> getRewardById(@PathVariable String id) {
        Optional<Reward> reward = rewardRepository.findById(id);

        return reward.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}