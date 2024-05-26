package com.mymerit.mymerit.domain.entity;

import com.mymerit.mymerit.domain.models.ProgrammingLanguage;
import lombok.Data;

@Data
public class TemplateFile {
    private ProgrammingLanguage language;
    private String name;
    private String contentBase64;
}
