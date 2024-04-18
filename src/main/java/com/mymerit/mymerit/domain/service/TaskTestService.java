package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.request.JudgeTokenRequest;
import com.mymerit.mymerit.api.payload.response.JudgeCompilationResponse;
import com.mymerit.mymerit.api.payload.response.TestResponse;
import com.mymerit.mymerit.domain.entity.*;
import com.mymerit.mymerit.infrastructure.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskTestService {
    TaskRepository taskRepository;
    JudgeService judgeService;

    TaskTestService(TaskRepository taskRepository,JudgeService judgeService){
        this.judgeService = judgeService;
        this.taskRepository = taskRepository;
    }

    public List<TestResponse> testResults(JudgeTokenRequest userRequest, String taskId,String language){


        Task task = taskRepository.findById(taskId).get();



        List<TestResponse> result = new ArrayList<>();

        List<CodeTest> tests = taskRepository.findById(taskId).get().getTests();

        CodeTest test = tests.stream().filter(l->l.getLanguage() == language).findFirst().orElseThrow(()->new RuntimeException("error"));

        userRequest.setFileContentBase64(test.getTestFile());

        for (TestCase testCase : test.getTestList()) {

            String input = testCase.getInput();
            String output = testCase.getExpectedOutput();

            userRequest.setStdin(input);
            userRequest.setExpectedOutput(output);

            JudgeCompilationResponse response = judgeService.generateRequestResponse(judgeService.generateTokenRequest(userRequest));

             TestResponse testResponse = new TestResponse();

            if(response.getStatus().getId() == 2){
                testResponse.setEvaluation(true);
                testResponse.setName(testCase.getName());
            }

            else{
                testResponse.setEvaluation(false);
                testResponse.setName(testCase.getName());
            }

            result.add(testResponse);

        }

        return result;
    }

    public TestResponse singleTest(JudgeTokenRequest judgeTokenRequest, String taskId, String language,Integer index){

        Task task = taskRepository.findById(taskId).get();

        List<TestResponse> result = new ArrayList<>();

        TestResponse testResult = new TestResponse();

        List<CodeTest> tests = taskRepository.findById(taskId).get().getTests();

        CodeTest test = tests.stream().filter(l->l.getLanguage() == language).findFirst().orElseThrow(()->new RuntimeException("error"));

        judgeTokenRequest.setFileContentBase64(test.getTestFile());

        TestCase testCase = test.getTestList().get(index);

        JudgeCompilationResponse response = judgeService.generateRequestResponse(judgeService.generateTokenRequest(judgeTokenRequest));

        String input = testCase.getInput();
        String output = testCase.getExpectedOutput();

        judgeTokenRequest.setStdin(input);
        judgeTokenRequest.setExpectedOutput(output);

        if(response.getStatus().getId() == 2){
            testResult.setEvaluation(true);
            testResult.setName(testCase.getName());
        }

        else{
            testResult.setEvaluation(false);
            testResult.setName(testCase.getName());
        }

            return testResult;

    }


}
