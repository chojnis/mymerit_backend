package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.response.RankingResponse;
import com.mymerit.mymerit.domain.entity.User;
import com.mymerit.mymerit.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingService {

    private UserRepository userRepository;

    public RankingService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<RankingResponse> getWeeklyRanking() {

        List<User> allUsers = userRepository.findAll();
        List<RankingResponse> response = new ArrayList<>();


        for (User user : allUsers) {
            Integer weeklyRanking = user.getRanking().getWeeklyRanking();
            RankingResponse rankingResponse = new RankingResponse(user, weeklyRanking);
            response.add(rankingResponse);
        }

        response.sort(Comparator.comparingInt(RankingResponse::getRanking).reversed());

        return  response.stream().limit(50).collect(Collectors.toList());
    }
    public List<RankingResponse> getAllTimeRanking(){

        List<User> allUsers = userRepository.findAll();
        List<RankingResponse> response = new ArrayList<>();


        for (User user : allUsers) {
            Integer weeklyRanking = user.getRanking().getRanking();
            RankingResponse rankingResponse = new RankingResponse(user, weeklyRanking);
            response.add(rankingResponse);
        }


        response.sort(Comparator.comparingInt(RankingResponse::getRanking).reversed());


        return  response.stream().limit(50).collect(Collectors.toList());

    }
}
