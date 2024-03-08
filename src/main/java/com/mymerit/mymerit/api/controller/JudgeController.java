package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.request.JudgeTokenRequest;
import com.mymerit.mymerit.api.payload.response.JudgeCompilationResponse;
import com.mymerit.mymerit.domain.service.JudgeService;
import org.springframework.web.bind.annotation.*;

@RestController
public class JudgeController {
    JudgeService judgeService;
    JudgeController(JudgeService judgeService){
        this.judgeService = judgeService;
    }

    @PostMapping("/token")
    private String getToken(@RequestBody JudgeTokenRequest fileRequest){
        return judgeService.generateTokenRequest(fileRequest);
    }

    @GetMapping("/token/{token}")
    private JudgeCompilationResponse requestHandler(@PathVariable String token){
        return judgeService.generateRequestResponse(token);
    }
}