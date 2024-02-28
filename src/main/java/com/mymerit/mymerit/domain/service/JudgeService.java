package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.api.payload.request.JudgeTokenHandlerRequest;
import com.mymerit.mymerit.api.payload.request.JudgeTokenRequest;
import com.mymerit.mymerit.api.payload.request.SingleFileJudgeRequest;
import com.mymerit.mymerit.domain.entity.File;
import com.mymerit.mymerit.infrastructure.utils.JudgeUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class JudgeService {

    public String generateTokenRequest(SingleFileJudgeRequest fileRequest) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:2358/submissions/?base64_encoded=true&wait=true";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody  = "{\n" +
                "  \"source_code\": \"" + fileRequest.getFileContentBase64() + "\",\n" +
                "  \"language_id\": " + JudgeUtils.getLanguageNumber(fileRequest.getFileName()) + "\n" +
                "}";

        System.out.println(requestBody);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<JudgeTokenRequest> response = restTemplate.postForEntity(apiUrl, request, JudgeTokenRequest.class);

        System.out.println(response.getBody().getToken());

        return response.getBody().getToken();
    }


    public JudgeTokenHandlerRequest generateRequestResponse(String token){

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:2358/submissions/"+token+"?base64_encoded=true";


        ResponseEntity<JudgeTokenHandlerRequest> response = restTemplate.getForEntity(apiUrl, JudgeTokenHandlerRequest.class);
        System.out.println(response.getBody().getStatus_id());

        return response.getBody();
    }


}


