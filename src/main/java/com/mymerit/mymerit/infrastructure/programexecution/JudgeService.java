package com.mymerit.mymerit.infrastructure.programexecution;

import com.mymerit.mymerit.api.payload.request.JudgeTokenEntity;
import com.mymerit.mymerit.api.payload.response.JudgeCompilationResponse;
import com.mymerit.mymerit.domain.programexecution.ExecuteProgramRequest;
import com.mymerit.mymerit.domain.programexecution.ExecuteProgramResult;
import com.mymerit.mymerit.domain.programexecution.ProgramExecutionId;
import com.mymerit.mymerit.domain.programexecution.ProgramExecutionModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
public class JudgeService implements ProgramExecutionModule {

    @Override
    public ProgramExecutionId execute(ExecuteProgramRequest request) {
        var generatedTokenRequest = generateTokenRequest(request);
        return new ProgramExecutionId(generatedTokenRequest);
    }

    @Override
    public ExecuteProgramResult getResult(ProgramExecutionId programExecutionId) {
        var judgeExecutionResponse = generateRequestResponse(programExecutionId.getValue());
        return new ExecuteProgramResult(judgeExecutionResponse.getCompile_output());
    }

    private String generateTokenRequest(ExecuteProgramRequest request) {

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:2358/submissions/?base64_encoded=true&wait=true";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\n" +
                "  \"additional_files\": \"" + encodeAsBase64(request.getProgramFiles()) + "\",\n" +
                "  \"language_id\": " + 89 + "\n" +
                "}";

        System.out.println(requestBody);

        ResponseEntity<JudgeTokenEntity> response = restTemplate.postForEntity(
                apiUrl,
                new HttpEntity<>(requestBody, headers),
                JudgeTokenEntity.class
        );

        System.out.println(response.getBody().getToken());

        return response.getBody().getToken();
    }

    private JudgeCompilationResponse generateRequestResponse(String token) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:2358/submissions/"+token+"?base64_encoded=true&stdout";

        ResponseEntity<JudgeCompilationResponse> response = restTemplate.getForEntity(apiUrl, JudgeCompilationResponse.class);
        System.out.println(response.getBody().getStatus_id());

        return response.getBody();
    }

    private String encodeAsBase64(Set<String> programFiles) {
        return "";
    }

}
