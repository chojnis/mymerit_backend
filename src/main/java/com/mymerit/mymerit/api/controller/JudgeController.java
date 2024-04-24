package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.request.JudgeTokenRequest;
import com.mymerit.mymerit.api.payload.response.JudgeCompilationResponse;
import com.mymerit.mymerit.api.payload.response.TestResponse;
import com.mymerit.mymerit.domain.service.JudgeService;
import com.mymerit.mymerit.domain.service.TaskTestService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class JudgeController {
    JudgeService judgeService;
    TaskTestService taskTestService;


    public JudgeController(JudgeService judgeService, TaskTestService taskTestService) {
        this.judgeService = judgeService;
        this.taskTestService = taskTestService;
    }


    @PostMapping("/test/task/{taskId}/language/{language}")
    private List<TestResponse> testAllCases(@RequestBody JudgeTokenRequest judgeTokenRequest, @PathVariable String taskId, @PathVariable String language){
        return taskTestService.testResults(judgeTokenRequest, taskId,language);

    }

    @PostMapping("/test/task/{taskId}/language/{language}/{testIndex}")
    private List<TestResponse> testSingleCase(@RequestBody JudgeTokenRequest judgeTokenRequest, @PathVariable String taskId, @PathVariable String language, @PathVariable Integer testIndex){
        return Collections.singletonList(taskTestService.singleTest(judgeTokenRequest, taskId, language, testIndex));
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
