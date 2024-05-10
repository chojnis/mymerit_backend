package com.mymerit.mymerit.api.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor

public class JudgeTokenRequest {
    @NotBlank
    String fileName;
    @NotBlank
    String fileContentBase64;
    Float timeLimit;
    Integer memoryLimit;
    String stdin;
    String expectedOutput;

    public JudgeTokenRequest(String fileName, String fileContentBase64, Float timeLimit, Integer memoryLimit, String stdin, String expectedOutput) {
        this.fileName = fileName;
        this.fileContentBase64 = fileContentBase64;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.stdin = stdin;
        this.expectedOutput = expectedOutput;
    }

    public JudgeTokenRequest(String fileName, String fileContentBase64) {
        this.fileName = fileName;
        this.fileContentBase64 = fileContentBase64;
    }
}
