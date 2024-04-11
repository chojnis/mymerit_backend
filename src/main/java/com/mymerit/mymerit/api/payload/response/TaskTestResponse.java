package com.mymerit.mymerit.api.payload.response;

public class TaskTestResponse {


    public String stderr;
    public Integer exit_code;
    public Float time;

    public TaskTestResponse(String stderr, Integer exit_code, Float time) {
        this.stderr = stderr;
        this.exit_code = exit_code;
        this.time = time;
    }
}
