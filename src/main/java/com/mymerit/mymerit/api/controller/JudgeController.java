package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.request.JudgeParams;
import com.mymerit.mymerit.api.payload.request.JudgeTokenRequest;
import com.mymerit.mymerit.api.payload.request.SolutionRequest;
import com.mymerit.mymerit.api.payload.response.JudgeCompilationResponse;
import com.mymerit.mymerit.api.payload.response.TestResponse;
import com.mymerit.mymerit.domain.models.ProgrammingLanguage;
import com.mymerit.mymerit.domain.service.JudgeService;
import com.mymerit.mymerit.domain.service.TaskTestService;
import com.mymerit.mymerit.infrastructure.utils.ZipUtility;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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


    @PostMapping("/compile-code")
    private ResponseEntity<JudgeCompilationResponse> compileCode(@RequestParam ProgrammingLanguage language, @RequestParam String mainFileName, @RequestBody List<MultipartFile>files,
                                                                  JudgeParams judgeParams) throws IOException {
        System.out.println(judgeParams);
        return ResponseEntity.ok(judgeService.getResponseFromMultipartFiles(files,mainFileName,judgeParams,language));
    }


    @PostMapping("/test/task/{taskId}/language/{language}")
    private List<TestResponse> testAllCases(@RequestParam List<MultipartFile>files, @PathVariable String taskId, @PathVariable ProgrammingLanguage language) throws IOException {
        return taskTestService.executeAllTests(files, taskId,language);

    }

    @PostMapping("/test/task/{taskId}/language/{language}/{testIndex}")
    private List<TestResponse> testSingleCase(@RequestParam List<MultipartFile>files, @PathVariable String taskId, @PathVariable ProgrammingLanguage language, @PathVariable Integer testIndex) throws IOException {
        return Collections.singletonList(taskTestService.executeSingleTest(taskId, language, files, testIndex));
    }



    @PostMapping("/token")
    private String getToken(@RequestBody JudgeTokenRequest fileRequest){
        return judgeService.generateTokenRequest(fileRequest);
    }

    @GetMapping("/token/{token}")
    private JudgeCompilationResponse requestHandler(@PathVariable String token){
        return judgeService.generateRequestResponse(token);
    }

    @PostMapping("/generateZipBaseEncode/{language}")
    private ResponseEntity<?> generateEncoded(@RequestBody SolutionRequest solutionRequest, @PathVariable ProgrammingLanguage language) throws IOException, IOException {
        return ResponseEntity.ok(ZipUtility.zipSolutionFilesAsBase64(solutionRequest,language));
    }
}
