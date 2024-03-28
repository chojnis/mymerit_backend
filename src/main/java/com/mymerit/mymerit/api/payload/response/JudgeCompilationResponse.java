package com.mymerit.mymerit.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
public class JudgeCompilationResponse implements Serializable {
    @Getter
    @Setter
    @AllArgsConstructor
    static class Status{
        public Integer id;
        public String description;
    }
    private String stdout;
    private int language_id;
    private String stderr;
    private String compile_output;
    private Status status;
    private Integer exit_code;
    private Float time;

}
