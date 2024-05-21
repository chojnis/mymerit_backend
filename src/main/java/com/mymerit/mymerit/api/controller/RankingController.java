package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.response.RankingResponse;
import com.mymerit.mymerit.domain.service.RankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/ranking")
public class RankingController {

    RankingService rankingService;

    public RankingController(RankingService rankingService){
        this.rankingService = rankingService;
    }


    @GetMapping("/weekly")
    public ResponseEntity<List<RankingResponse>> getWeeklyRanking(){
        return ResponseEntity.ok(rankingService.getWeeklyRanking());
    }

    @GetMapping("/allTime")
    public ResponseEntity<List<RankingResponse>> getAllTimeRanking(){
        return ResponseEntity.ok(rankingService.getAllTimeRanking());
    }

}
