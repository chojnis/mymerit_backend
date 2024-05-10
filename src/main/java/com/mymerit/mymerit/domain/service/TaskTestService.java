package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.request.JudgeTokenRequest;
import com.mymerit.mymerit.api.payload.response.JudgeCompilationResponse;
import com.mymerit.mymerit.api.payload.response.TestResponse;
import com.mymerit.mymerit.domain.entity.*;
import com.mymerit.mymerit.domain.models.ProgrammingLanguage;
import com.mymerit.mymerit.infrastructure.repository.TaskRepository;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class TaskTestService {
    TaskRepository taskRepository;
    JudgeService judgeService;

    TaskTestService(TaskRepository taskRepository,JudgeService judgeService){
        this.judgeService = judgeService;
        this.taskRepository = taskRepository;
    }

    public List<TestResponse> runAllTests(JudgeTokenRequest userRequest, String taskId, ProgrammingLanguage language){
        List<TestResponse> result = new ArrayList<>();
        List<CodeTest> tests = taskRepository.findById(taskId).get().getTests();

        CodeTest test = tests.stream()
                .filter(l-> Objects.equals(l.getLanguage(), language))
                .findFirst()
                .orElseThrow(()->new RuntimeException("Couldn't fidn test for language"+ language));

        //userRequest.setFileContentBase64(test.getTestFileBase64());

        for (TestCase testCase : test.getTestCases()) {
            String input = Base64.getEncoder().encodeToString(testCase.getInput().getBytes());
            String output = Base64.getEncoder().encodeToString(testCase.getExpectedOutput().getBytes());

            userRequest.setStdin(input);
            userRequest.setExpectedOutput(output);

            JudgeCompilationResponse response = judgeService.generateRequestResponse(judgeService.generateTokenRequest(userRequest));
            TestResponse testResponse = new TestResponse();
            System.out.println(response);
            testResponse.setName(testCase.getName());
            testResponse.setPassed(response.getStatus().getId() == 3);

            result.add(testResponse);
        }
        return result;
    }

    public TestResponse runSingleTest(JudgeTokenRequest judgeTokenRequest, String taskId, ProgrammingLanguage language, Integer index){//we zrob jakos podfunkcje bo duzo sie powtarza
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

    public TestResponse executeSingleTest(String taskId, ProgrammingLanguage language, List<MultipartFile> files, Integer testIndex) throws IOException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()->new RuntimeException("Task for id " + taskId + " not found"));

        Optional<String> optionalTestFileBase64 = task.getTestByLanguage(language)
                .map(CodeTest::getTestFileBase64);

        if (optionalTestFileBase64.isEmpty()) {
            throw new RuntimeException("Test file not available for language: " + language);
        }

        String testFileBase64 = optionalTestFileBase64.get();

        String mainFileName = "MainTestFile." + language.getExtension();
        files.add(convertBase64ToMultipartFile(mainFileName , testFileBase64));
        String encodedFiles = judgeService.encodeFromMultifile(files,mainFileName,language);
        JudgeTokenRequest judgeTokenRequest = new JudgeTokenRequest(mainFileName,encodedFiles);
        return runSingleTest(judgeTokenRequest,task.getId(),language, testIndex);
    }

    public List<TestResponse> executeAllTests(List<MultipartFile> files, String taskId, ProgrammingLanguage language) throws IOException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()->new RuntimeException("Task for id " + taskId + " not found"));

        Optional<String> optionalTestFileBase64 = task.getTestByLanguage(language)
                .map(CodeTest::getTestFileBase64);

        if (optionalTestFileBase64.isEmpty()) {
            throw new RuntimeException("Test file not available for language: " + language);
        }

        String testFileBase64 = optionalTestFileBase64.get();

        String mainFileName = "MainTestFile." + language.getExtension();
        files.add(convertBase64ToMultipartFile(mainFileName , testFileBase64));
        String encodedFiles = judgeService.encodeFromMultifile(files,mainFileName,language);
        JudgeTokenRequest judgeTokenRequest = new JudgeTokenRequest(mainFileName,encodedFiles);
        return runAllTests(judgeTokenRequest,task.getId(),language);
    }

    private MultipartFile convertBase64ToMultipartFile(String fileName, String base64Data) {
        byte[] fileContent = Base64.getDecoder().decode(base64Data);
        return new MockMultipartFile(//narazie tak, pewnie zmienie
                "file",
                fileName,
                "text/plain",
                fileContent
        );
    }
}
