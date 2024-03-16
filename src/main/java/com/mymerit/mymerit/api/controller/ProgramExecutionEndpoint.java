package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.request.ProgramExecutionApiRequest;
import com.mymerit.mymerit.api.payload.response.ProgramExecutionApiResponse;
import com.mymerit.mymerit.domain.programexecution.ProgramExecutionId;
import com.mymerit.mymerit.domain.programexecution.ProgramExecutionModule;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProgramExecutionEndpoint {

    ProgramExecutionModule programExecutionModule;

    ProgramExecutionEndpoint(ProgramExecutionModule programExecutionModule) {
        this.programExecutionModule = programExecutionModule;
    }

    @PostMapping("/program/executions")
    private ProgramExecutionId execute(@RequestBody ProgramExecutionApiRequest programExecutionApiRequest) {
        return programExecutionModule.execute(programExecutionApiRequest.toDomain());
    }

    @GetMapping("/program/executions/{executionId}")
    private ProgramExecutionApiResponse getResult(@PathVariable String programExecutionId) {
        var programExecutionResult = programExecutionModule.getResult(ProgramExecutionId.from(programExecutionId));
        return new ProgramExecutionApiResponse(programExecutionResult.getCompileOutput());
    }

}
