package com.mymerit.mymerit.api.payload.request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class FileListRequest {
    private List<String> fileIDS;
}