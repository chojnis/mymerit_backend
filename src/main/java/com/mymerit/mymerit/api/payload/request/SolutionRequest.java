package com.mymerit.mymerit.api.payload.request;

import com.mymerit.mymerit.domain.entity.SolutionFile;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SolutionRequest {
     private List<SolutionFile> files;
     private String mainName;




}