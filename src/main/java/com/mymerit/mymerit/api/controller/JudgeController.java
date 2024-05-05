package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.request.JudgeParams;
import com.mymerit.mymerit.api.payload.request.JudgeTokenRequest;
import com.mymerit.mymerit.api.payload.request.SolutionRequest;
import com.mymerit.mymerit.api.payload.response.JudgeCompilationResponse;
import com.mymerit.mymerit.api.payload.response.TestResponse;
import com.mymerit.mymerit.domain.service.JudgeService;
import com.mymerit.mymerit.domain.service.TaskTestService;
import com.mymerit.mymerit.domain.service.UserDetailsImpl;
import com.mymerit.mymerit.infrastructure.utils.ZipUtility;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping("/token/{jobOfferId}")
    private String getToken(@RequestBody JudgeTokenRequest fileRequest, UserDetailsImpl userDetails){

        return judgeService.generateTokenRequest(fileRequest);
    }

    @GetMapping("/token/{token}")
    private JudgeCompilationResponse requestHandler(@PathVariable String token){
        return judgeService.generateRequestResponse(token);
    }

    @PostMapping("/generateZipBaseEncode/{language}")
    private ResponseEntity<?> generateEncoded(@RequestBody SolutionRequest solutionRequest, @PathVariable String language) throws IOException, IOException {

        return ResponseEntity.ok(ZipUtility.zipSolutionFilesAsBase64(solutionRequest,language));
    }

    @GetMapping("/xdxd")
        private ResponseEntity<?> gxed(@RequestParam String language, @RequestParam String mainName, @RequestBody List<MultipartFile>files,
                                       @RequestBody JudgeParams judgeParams) throws IOException {

        return ResponseEntity.ok(judgeService.getResponseFromMultipartFiles(files,mainName,judgeParams,language));
    }
}
