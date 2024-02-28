package com.mymerit.mymerit.api.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class JudgeCompilationResponse implements Serializable {

    @Getter @Setter
    private static class Status {
        private int id;
        private String description;
    }

    private String stdout;
    private String status_id;
    private int language_id;
    private String stderr;
    private String compile_output;
    private Status status;

    public JudgeCompilationResponse(String stdout, String status_id, int language_id, String stderr, String compile_output, Status status) {
        this.stdout = stdout;
        this.status_id = status_id;
        this.language_id = language_id;
        this.stderr = stderr;
        this.compile_output = compile_output;
        this.status = status;
    }
}
