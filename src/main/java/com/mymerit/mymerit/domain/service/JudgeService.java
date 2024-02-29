package com.mymerit.mymerit.domain.service;

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

        String requestBody;
        if(judgeTokenRequest.getIsMultiFile()){
            requestBody  = "{\n" +
                    "  \"additional_files\": \"" + judgeTokenRequest.getFileContentBase64() + "\",\n" +
                    "  \"language_id\": " + 89 + "\n" +
                    "}";
        }else{
            requestBody  = "{\n" +
                    "  \"source_code\": \"" + judgeTokenRequest.getFileContentBase64() + "\",\n" +
                    "  \"language_id\": " + JudgeUtils.getLanguageNumber(judgeTokenRequest.getFileName()) + "\n" +
                    "}";
        }

        System.out.println(requestBody);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<JudgeTokenEntity> response = restTemplate.postForEntity(apiUrl, request, JudgeTokenEntity.class);

        System.out.println(response.getBody().getToken());

        return response.getBody().getToken();
    }


    public JudgeCompilationResponse generateRequestResponse(String token){

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:2358/submissions/"+token+"?base64_encoded=true&stdout";


        ResponseEntity<JudgeCompilationResponse> response = restTemplate.getForEntity(apiUrl, JudgeCompilationResponse.class);
        System.out.println(response.getBody().getStatus_id());

        return response.getBody();
    }


}


