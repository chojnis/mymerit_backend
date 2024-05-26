package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.response.RewardHistoryResponse;
import com.mymerit.mymerit.domain.entity.Reward;
import com.mymerit.mymerit.domain.entity.RewardHistory;
import com.mymerit.mymerit.domain.entity.User;
import com.mymerit.mymerit.domain.exception.InsufficientCreditsException;
import com.mymerit.mymerit.domain.exception.RewardNotFoundException;
import com.mymerit.mymerit.domain.exception.UserNotFoundException;
import com.mymerit.mymerit.infrastructure.repository.RewardHistoryRepository;
import com.mymerit.mymerit.infrastructure.repository.RewardRepository;
import com.mymerit.mymerit.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class RewardService {
    private final UserRepository userRepository;
    private final RewardRepository rewardRepository;
    private final RewardHistoryRepository rewardHistoryRepository;
    private final MailSenderService mailSenderService;

    public RewardService(UserRepository userRepository, RewardRepository rewardRepository, RewardHistoryRepository rewardHistoryRepository, MailSenderService mailSenderService) {
        this.userRepository = userRepository;
        this.rewardRepository = rewardRepository;
        this.rewardHistoryRepository = rewardHistoryRepository;
        this.mailSenderService = mailSenderService;
    }

    public Reward purchaseReward(String userId, String rewardId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User " + userId + " not found"));

        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new RewardNotFoundException("Reward " + rewardId + " not found"));

        if (user.getCredits() < reward.getCost()) {
            throw new InsufficientCreditsException("Insufficient credits for user " + userId);
        }

        user.setCredits(user.getCredits() - reward.getCost());
        userRepository.save(user);

        RewardHistory rewardHistory = new RewardHistory();
        rewardHistory.setUser(user);
        rewardHistory.setReward(reward);
        rewardHistory.setDatePurchase(LocalDateTime.now());
        rewardHistoryRepository.save(rewardHistory);

        String productKey = generateKey();
        mailSenderService.sendReward(user, reward, productKey);

        return reward;
    }

    public List<RewardHistoryResponse> getCurrentUserRewardHistory(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User " + userId + " not found"));

        List<RewardHistory> userRewards = rewardHistoryRepository.findByUser(user)
                .orElse(Collections.emptyList());

        return userRewards.stream()
                .map(rewardHistory -> new RewardHistoryResponse(rewardHistory.getReward(), rewardHistory.getDatePurchase()))
                .collect(Collectors.toList());
    }

    private String generateKey() {
        StringBuilder keyBuilder = new StringBuilder();
        Random random = new Random();

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                keyBuilder.append(characters.charAt(random.nextInt(characters.length())));
            }

            if (i < 3) {
                keyBuilder.append("-");
            }
        }

        return keyBuilder.toString();
    }
}
