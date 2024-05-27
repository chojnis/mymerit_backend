package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.response.RankingResponse;
import com.mymerit.mymerit.domain.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/ranking")
@Tag(name = "RankingController")
public class RankingController {

    RankingService rankingService;

    public RankingController(RankingService rankingService){
        this.rankingService = rankingService;
    }


    @Operation(
            summary = "Returns weekly ranking")
    @GetMapping("/weekly")
    public ResponseEntity<List<RankingResponse>> getWeeklyRanking(){
        return ResponseEntity.ok(rankingService.getWeeklyRanking());
    }

    @Operation(
            summary = "Returns yearly ranking")
    @GetMapping("/yearly")
    public ResponseEntity<List<RankingResponse>> getYearalyRanking(){
        return ResponseEntity.ok(rankingService.getYearlyRanking());
    }


    @Operation(
            summary = "Returns monthly ranking")
    @GetMapping("/monthly")
    public ResponseEntity<List<RankingResponse>> getMonthlyRanking(){
        return ResponseEntity.ok(rankingService.getMonthlyRanking());
    }

    @Operation(
            summary = "Returns all time ranking")
    @GetMapping("/allTime")
    public ResponseEntity<List<RankingResponse>> getAllTimeRanking(){
        return ResponseEntity.ok(rankingService.getAllTimeRanking());
    }

}
