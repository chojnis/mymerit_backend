package com.mymerit.mymerit.api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JudgeParams {

    public String language;
    public String expected_output;
    public String stdin;
    public Integer memory_limit;
    public Float cpu_time_limit;
    public String command_line_arguments;

    public JudgeParams(String language, Integer memory_limit, Float cpu_time_limit) {
        this.language = language;
        this.memory_limit = memory_limit;
        this.cpu_time_limit = cpu_time_limit;
    }
}
