package com.mymerit.mymerit.domain.models;

public enum ProgrammingLanguage {
    JAVA("java"),
    PYTHON("py"),
    JAVASCRIPT("js"),
    CPP("cpp"),
    GO("go"),
    KOTLIN("kt"),
    TYPESCRIPT("ts"),
    PHP("php");

    private final String extension;

    ProgrammingLanguage(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public String getExtensionWithDot() {
        return "." + extension;
    }
}