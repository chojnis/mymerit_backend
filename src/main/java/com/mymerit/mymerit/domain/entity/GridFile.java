package com.mymerit.mymerit.domain.entity;

import lombok.Data;

@Data
public class GridFile {
    private String filename;
    private String fileType;
    private String fileSize;
    private byte[] file;
}