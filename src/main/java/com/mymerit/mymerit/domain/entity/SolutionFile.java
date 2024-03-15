package com.mymerit.mymerit.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class SolutionFile {
    @Id
    String solutionId;
    String fileId;
    String name;
    String content;
    Boolean isMain;

    public SolutionFile(String fileId, String name, String content, Boolean isMain) {
        this.fileId = fileId;
        this.name = name;
        this.content = content;
        this.isMain = isMain;
    }
}
