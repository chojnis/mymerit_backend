package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.request.JudgeTokenRequest;
import com.mymerit.mymerit.api.payload.response.JudgeCompilationResponse;
import com.mymerit.mymerit.api.payload.response.TestResponse;
import com.mymerit.mymerit.domain.entity.*;
import com.mymerit.mymerit.infrastructure.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
public class TaskTestService {
    TaskRepository taskRepository;
    JudgeService judgeService;

    TaskTestService(TaskRepository taskRepository,JudgeService judgeService){
        this.judgeService = judgeService;
        this.taskRepository = taskRepository;
    }

    public List<TestResponse> testResults(JudgeTokenRequest userRequest, String taskId,String language){
        List<TestResponse> result = new ArrayList<>();
        List<CodeTest> tests = taskRepository.findById(taskId).get().getTests();

        CodeTest test = tests.stream()
                .filter(l-> Objects.equals(l.getLanguage(), language))
                .findFirst()
                .orElseThrow(()->new RuntimeException("error"));

        //userRequest.setFileContentBase64(test.getTestFileBase64());

        for (TestCase testCase : test.getTestCases()) {
            String input = Base64.getEncoder().encodeToString(testCase.getInput().getBytes());
            String output = Base64.getEncoder().encodeToString(testCase.getExpectedOutput().getBytes());

            userRequest.setStdin(input);
            userRequest.setExpectedOutput(output);

            JudgeCompilationResponse response = judgeService.generateRequestResponse(judgeService.generateTokenRequest(userRequest));
            TestResponse testResponse = new TestResponse();

            testResponse.setName(testCase.getName());
            testResponse.setPassed(response.getStatus().getId() == 3);

            result.add(testResponse);
        }
        return result;
    }

    public TestResponse singleTest(JudgeTokenRequest judgeTokenRequest, String taskId, String language,Integer index){//we zrob jakos podfunkcje bo duzo sie powtarza
        TestResponse testResult = new TestResponse();

        List<CodeTest> tests = taskRepository.findById(taskId).get().getTests();

        CodeTest test = tests.stream()
                .filter(l-> Objects.equals(l.getLanguage(), language))
                .findFirst()
                .orElseThrow(()->new RuntimeException("error"));//komentarz
        TestCase testCase = test.getTestCases().get(index);
        String input = Base64.getEncoder().encodeToString(testCase.getInput().getBytes());
        String output = Base64.getEncoder().encodeToString(testCase.getExpectedOutput().getBytes());

        judgeTokenRequest.setStdin(input);
        judgeTokenRequest.setExpectedOutput(output);

        JudgeCompilationResponse response = judgeService.generateRequestResponse(judgeService.generateTokenRequest(judgeTokenRequest));

        testResult.setName(testCase.getName());
        testResult.setPassed(response.getStatus().getId() == 3);

        return testResult;
    }


}
