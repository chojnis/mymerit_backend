package com.mymerit.mymerit.domain.entity;

import lombok.Data;

@Data
public class DownloadFile {
    private String filename;
    private String fileType;
    private String fileSize;
    private byte[] file;
}