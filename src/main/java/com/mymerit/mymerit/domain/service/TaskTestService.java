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
            System.out.println(response);
            TestResponse testResponse = new TestResponse();
            System.out.println("\n\n\n");
            System.out.println(response.getStatus().getId());
            if(response.getStatus().getId() == 3){
                testResponse.setPassed(true);
                testResponse.setName(testCase.getName());
            }

            else{
                testResponse.setPassed(false);
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

        judgeTokenRequest.setFileContentBase64(test.getTestFileBase64());

        TestCase testCase = test.getTestCases().get(index);


        String input = testCase.getInput();
        String output = testCase.getExpectedOutput();

        judgeTokenRequest.setStdin(input);
        judgeTokenRequest.setExpectedOutput(output);

        JudgeCompilationResponse response = judgeService.generateRequestResponse(judgeService.generateTokenRequest(judgeTokenRequest));


        if(response.getStatus().getId() == 3){
            testResult.setPassed(true);
            testResult.setName(testCase.getName());
        }



        else{
            testResult.setPassed(false);
            testResult.setName(testCase.getName());
        }

            return testResult;

    }


}
