package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.response.ApiResponse;
import com.mymerit.mymerit.domain.entity.Rewards;
import com.mymerit.mymerit.domain.entity.User;
import com.mymerit.mymerit.domain.service.MailSenderService;
import com.mymerit.mymerit.domain.service.UserDetailsImpl;
import com.mymerit.mymerit.infrastructure.repository.RewardsRepository;
import com.mymerit.mymerit.infrastructure.repository.UserRepository;
import com.mymerit.mymerit.infrastructure.security.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class RewardsController {
    private final UserRepository userRepository;

    private final RewardsRepository rewardRepository;

    private final MailSenderService mailService;

    public RewardsController(UserRepository userRepository, RewardsRepository rewardRepository, MailSenderService mailService) {
        this.userRepository = userRepository;
        this.rewardRepository = rewardRepository;
        this.mailService = mailService;
    }

    @GetMapping("/reward/{reward_id}")
    public ResponseEntity<?> grant_reward(@PathVariable String reward_id, @CurrentUser UserDetailsImpl userDetails){
        String user_id = userDetails.getId();
        String user_mail = userDetails.getEmail();
        Rewards chosenReward = rewardRepository.findById(reward_id).get();
        User user  = userRepository.findById(user_id).get();
        String reward_name = chosenReward.getName();
        int user_points = Integer.valueOf(user.getPoints() );
        int reward_cost = Integer.valueOf(chosenReward.getCost() );
        if( reward_cost > user_points){
            return ResponseEntity
                        .badRequest()
                        .body(new ApiResponse(false, "not enough points"));
        }
        user.setPoints(Integer.valueOf(String.valueOf(user_points - reward_cost)));

        mailService.sendReward(reward_name, user_mail);
        return ResponseEntity
                        .ok()
                        .body(new ApiResponse(true, "granted reward"));
    }
}