package com.mymerit.mymerit.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;
@Data
@AllArgsConstructor

public class CodeTest {

    private String language;

    @DBRef
    @JsonIgnore
    private Task taskId;



    private List<TestCase> testList;

}
