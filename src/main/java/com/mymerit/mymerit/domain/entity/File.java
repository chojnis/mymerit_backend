package com.mymerit.mymerit.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("files")
public class File {

    @Id
    private Long id;
    private String fileName;

    private Integer language;
    private String fileContent;

    public File(String fileName, Integer language, String fileContent) {
        this.fileName = fileName;

        this.language = language;
        this.fileContent = fileContent;
    }
}
