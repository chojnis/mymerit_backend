package com.mymerit.mymerit.api.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class JudgeCompilationResponse implements Serializable {
    @Getter
    @Setter
    static class Status{
        public Integer id;
        public String description;
    }
    private String stdout;
    private int language_id;
    private String stderr;
    private String compile_output;
    private Status status;

    public JudgeCompilationResponse(String stdout, String status_id, int language_id, String stderr, String compile_output, Status status) {
        this.stdout = stdout;
        this.language_id = language_id;
        this.stderr = stderr;
        this.compile_output = compile_output;
        this.status = status;
    }
}
