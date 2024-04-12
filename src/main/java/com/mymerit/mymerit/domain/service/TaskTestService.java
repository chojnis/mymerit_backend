package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.request.JudgeTokenRequest;
import com.mymerit.mymerit.api.payload.response.JudgeCompilationResponse;
import com.mymerit.mymerit.api.payload.response.TaskTestResponse;
import com.mymerit.mymerit.domain.entity.Solution;
import com.mymerit.mymerit.domain.entity.SolutionFile;
import com.mymerit.mymerit.domain.entity.Task;
import com.mymerit.mymerit.infrastructure.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
public class TaskTestService {
    TaskRepository taskRepository;
    JudgeService judgeService;

    TaskTestService(TaskRepository taskRepository,JudgeService judgeService){
        this.judgeService = judgeService;
        this.taskRepository = taskRepository;
    }

    public List<JudgeCompilationResponse.Status> testResults(JudgeTokenRequest judgeTokenRequest, String taskId){
        Map<String,String> test = taskRepository.findById(taskId).get().getTestDataMap();
        List<JudgeCompilationResponse.Status> resultList = new ArrayList<>();
        for (Map.Entry<String, String> entry : test.entrySet()) {
            String stdin = entry.getKey();

            String expectedOutput = entry.getValue();

            judgeTokenRequest.setStdin(stdin);
            judgeTokenRequest.setExpectedOutput(expectedOutput);

            String token = judgeService.generateTokenRequest(judgeTokenRequest);

            JudgeCompilationResponse judgeCompilationResponse = judgeService.generateRequestResponse(token);

            TaskTestResponse solutionResponse = new TaskTestResponse(judgeCompilationResponse.getStderr(), judgeCompilationResponse.getExit_code(), judgeCompilationResponse.getTime());

            resultList.add(judgeCompilationResponse.getStatus());
        }
        return resultList;
    }

}
