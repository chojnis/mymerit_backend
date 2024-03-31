package com.mymerit.mymerit.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.core.io.ByteArrayResource;

import java.util.Base64;


@Data
@AllArgsConstructor
@ToString
public class DownloadFileResponse {
    private String name;
    private String type;
    private String contentBase64;

    public DownloadFileResponse(String filename, String fileType, byte[] fileContent) {
        this.name = filename;
        this.type = fileType;
        this.contentBase64 = Base64.getEncoder().encodeToString(fileContent);
    }
}