package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.request.JudgeTokenHandlerRequest;
import com.mymerit.mymerit.api.payload.request.JudgeTokenRequest;
import com.mymerit.mymerit.api.payload.request.SingleFileJudgeRequest;
import com.mymerit.mymerit.domain.entity.File;
import com.mymerit.mymerit.domain.service.JudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class JudgeController {
    JudgeService judgeService;
    JudgeController(JudgeService judgeService){
        this.judgeService = judgeService;
    }

    @PostMapping("/token")
    private String getToken(@RequestBody SingleFileJudgeRequest fileRequest){
        return judgeService.generateTokenRequest(fileRequest);
    }

    @GetMapping("/token/{token}")
    private JudgeTokenHandlerRequest requestHandler(@PathVariable String token){
        return judgeService.generateRequestResponse(token);
    }
}
