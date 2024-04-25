package com.mymerit.mymerit.domain.entity;

import lombok.Data;

@Data
public class ConfigFile {

    private String compile;
    private String run;

    public ConfigFile(String compile, String run) {
        this.compile = compile;
        this.run = run;
    }


    @Override
    public String toString() {
        return "ConfigFile{" +
                "compile='" + compile + '\'' +
                ", run='" + run + '\'' +
                '}';
    }
}
