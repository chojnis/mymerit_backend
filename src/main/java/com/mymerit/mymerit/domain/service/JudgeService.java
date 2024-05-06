package com.mymerit.mymerit.domain.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mymerit.mymerit.api.payload.request.JudgeTokenRequest;
import com.mymerit.mymerit.api.payload.response.JudgeCompilationResponse;
import com.mymerit.mymerit.api.payload.request.JudgeTokenEntity;
import com.mymerit.mymerit.infrastructure.utils.JudgeUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JudgeService {

    public String generateTokenRequest(JudgeTokenRequest judgeTokenRequest) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:2358/submissions/?base64_encoded=true&wait=true";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = toJsonString(judgeTokenRequest);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<JudgeTokenEntity> response = restTemplate.postForEntity(apiUrl, request, JudgeTokenEntity.class);

        return response.getBody().getToken();
    }

    public JudgeCompilationResponse generateRequestResponse(String token){
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:2358/submissions/" + token + "?base64_encoded=true&fields=stdout,time,stderr,token,compile_output,message,status,exit_code";
        ResponseEntity<JudgeCompilationResponse> response = restTemplate.getForEntity(apiUrl, JudgeCompilationResponse.class);
        return response.getBody();
    }

    public String toJsonString(JudgeTokenRequest judgeTokenRequest) {
        StringBuilder requestBodyBuilder = new StringBuilder("{\n");

        if (judgeTokenRequest.getFileName() != null) {
            requestBodyBuilder
                    .append("  \"additional_files\": \"")
                    .append(judgeTokenRequest.getFileContentBase64())
                    .append("\",\n");
        }
        if (judgeTokenRequest.getFileName() != null) {
            requestBodyBuilder
                    .append("  \"language_id\": \"")
                    .append(89)
                    .append("\",\n");
        }
        if (judgeTokenRequest.getCommandLineArguments() != null) {
            requestBodyBuilder
                    .append("  \"command_line_arguments\": \"")
                    .append(judgeTokenRequest.getCommandLineArguments())
                    .append("\",\n");
        }
        if (judgeTokenRequest.getTimeLimit() != null) {
            requestBodyBuilder
                    .append("  \"cpu_time_limit\": ")
                    .append(judgeTokenRequest.getTimeLimit())
                    .append(",\n");
        }
        if (judgeTokenRequest.getMemoryLimit() != null) {
            requestBodyBuilder
                    .append("  \"memory_limit\": ")
                    .append(judgeTokenRequest.getMemoryLimit())
                    .append(",\n");
        }
        if (judgeTokenRequest.getStdin() != null) {
            requestBodyBuilder
                    .append("  \"stdin\": \"")
                    .append(judgeTokenRequest.getStdin())
                    .append("\",\n");
        }
        if (judgeTokenRequest.getExpectedOutput() != null) {
            requestBodyBuilder
                    .append("  \"expected_output\": \"")
                    .append(judgeTokenRequest.getExpectedOutput())
                    .append("\",\n");
        }

        if (requestBodyBuilder.charAt(requestBodyBuilder.length() - 2) == ',') {
            requestBodyBuilder.deleteCharAt(requestBodyBuilder.length() - 2);
        }

        requestBodyBuilder.append("}");

        return requestBodyBuilder.toString();
    }
}


