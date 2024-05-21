package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.response.RankingResponse;
import com.mymerit.mymerit.domain.entity.User;
import com.mymerit.mymerit.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class RankingService {

    private UserRepository userRepository;

    public RankingService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<RankingResponse> getWeeklyRanking() {
        AtomicInteger rank = new AtomicInteger(1);
        return userRepository.findAll().stream()
                .filter(user -> user.getRole().equals("user"))
                .sorted(Comparator.<User, Integer>comparing(
                        user -> user.getRanking().getWeeklyRanking()).reversed())
                .map(user -> new RankingResponse(
                        user,
                        user.getRanking().getWeeklyRanking(),
                        rank.getAndIncrement()))
                .limit(50)
                .toList();
    }


    public List<RankingResponse> getMonthlyRanking() {
        AtomicInteger rank = new AtomicInteger(1);
        return userRepository.findAll().stream()
                .filter(user -> user.getRole().equals("user"))
                .sorted(Comparator.<User, Integer>comparing(
                        user -> user.getRanking().getMonthlyRanking()).reversed())
                .map(user -> new RankingResponse(
                        user,
                        user.getRanking().getMonthlyRanking(),
                        rank.getAndIncrement()))
                .limit(50)
                .toList();
    }

    public List<RankingResponse> getYearlyRanking() {
        AtomicInteger rank = new AtomicInteger(1);
        return userRepository.findAll().stream()
                .filter(user -> user.getRole().equals("user"))
                .sorted(Comparator.<User, Integer>comparing(
                        user -> user.getRanking().getYearlyRanking()).reversed())
                .map(user -> new RankingResponse(
                        user,
                        user.getRanking().getYearlyRanking(),
                        rank.getAndIncrement()))
                .limit(50)
                .toList();
    }


    public List<RankingResponse> getAllTimeRanking() {
        AtomicInteger rank = new AtomicInteger(1);
        return userRepository.findAll().stream()
                .sorted(Comparator.<User, Integer>comparing(
                        user -> user.getRanking().getAllTimeRanking()).reversed())
                .map(user -> new RankingResponse(
                        user,
                        user.getRanking().getAllTimeRanking(),
                        rank.getAndIncrement()))
                .limit(50)
                .toList();
    }
}
