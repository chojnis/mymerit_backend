package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.request.JudgeTokenRequest;
import com.mymerit.mymerit.api.payload.response.JudgeCompilationResponse;
import com.mymerit.mymerit.api.payload.response.TestResponse;
import com.mymerit.mymerit.domain.service.JudgeService;
import com.mymerit.mymerit.domain.service.TaskTestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JudgeController {

    JudgeService judgeService;
    TaskTestService taskTestService;


    public JudgeController(JudgeService judgeService, TaskTestService taskTestService) {
        this.judgeService = judgeService;
        this.taskTestService = taskTestService;
    }


    @PostMapping("/test/task/{taskId}")
    private List<TestResponse> tests(@RequestBody JudgeTokenRequest judgeTokenRequest, @PathVariable String taskId, @RequestBody String language){
        return taskTestService.testResults(judgeTokenRequest, taskId,language);

    }

    @PostMapping("/test/task/{taskId}")
    private TestResponse tests(@RequestBody JudgeTokenRequest judgeTokenRequest, @PathVariable String taskId, @RequestBody String language, @RequestBody Integer index){
        return taskTestService.singleTest(judgeTokenRequest, taskId,language,index);

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
