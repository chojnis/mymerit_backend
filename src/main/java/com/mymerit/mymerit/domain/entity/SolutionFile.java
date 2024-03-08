package com.mymerit.mymerit.domain.entity;

import lombok.Data;

@Data
public class SolutionFile {
    String solutionId;
    String fileId;
    String name;
    String content;
    Boolean isMain;


}
