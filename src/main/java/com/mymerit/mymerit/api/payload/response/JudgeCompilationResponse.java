package com.mymerit.mymerit.api.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class JudgeCompilationResponse implements Serializable {
    private String stdout;
    private String status_id;
    private int language_id;
    private String stderr;
    private String compile_output;


    public JudgeCompilationResponse(String stdout, String status_id, int language_id, String stderr, String compile_output) {
        this.stdout = stdout;
        this.status_id = status_id;
        this.language_id = language_id;
        this.stderr = stderr;
        this.compile_output = compile_output;
    }
}
