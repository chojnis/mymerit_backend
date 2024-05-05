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
    String language;
    String commandLineArguments;
    Float timeLimit;
    Integer memoryLimit;
    String stdin;
    String expectedOutput;

    public JudgeTokenRequest(String fileContentBase64, String stdin) {
        this.fileContentBase64 = fileContentBase64;
        this.stdin = stdin;
    }

    public JudgeTokenRequest(String fileName, String fileContentBase64, String commandLineArguments, Float timeLimit, Integer memoryLimit, String stdin, String expectedOutput) {
        this.fileName = fileName;
        this.fileContentBase64 = fileContentBase64;
        this.commandLineArguments = commandLineArguments;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.stdin = stdin;
        this.expectedOutput = expectedOutput;
    }

    public JudgeTokenRequest(String fileName, String fileContentBase64, String language) {
        this.fileName = fileName;
        this.fileContentBase64 = fileContentBase64;
        this.language = language;
    }
}
