package com.mymerit.mymerit.api.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class JudgeTokenRequest {
    @NotBlank
    String fileName;

    @NotBlank
    String fileContentBase64;

    String commandLineArguments;
    Float timeLimit;
    Integer memoryLimit;
    String stdin;
    String expectedOutput;

    public JudgeTokenRequest(String fileContentBase64, String stdin) {
        this.fileContentBase64 = fileContentBase64;
        this.stdin = stdin;
    }
}
