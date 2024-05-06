package com.mymerit.mymerit.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mymerit.mymerit.domain.models.ProgrammingLanguage;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;
@Data
@AllArgsConstructor

public class CodeTest {

    private ProgrammingLanguage language;

    @DBRef
    @JsonIgnore
    private Task taskId;

    private String testFileBase64;


    private List<TestCase> testCases;

}
