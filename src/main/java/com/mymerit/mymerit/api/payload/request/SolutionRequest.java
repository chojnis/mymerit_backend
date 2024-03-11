package com.mymerit.mymerit.api.payload.request;

import com.mymerit.mymerit.domain.entity.SolutionFile;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SolutionRequest {
     List<SolutionFile> files;
}

