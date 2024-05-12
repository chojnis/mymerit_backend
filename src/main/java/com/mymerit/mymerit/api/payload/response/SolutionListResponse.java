package com.mymerit.mymerit.api.payload.response;

import com.mymerit.mymerit.domain.entity.Feedback;
import com.mymerit.mymerit.domain.models.ProgrammingLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class SolutionListResponse {
    private String jobId;
    private String taskName;
    private LocalDateTime submitDate;
    private Feedback feedback;
    private ProgrammingLanguage solutionLanguage;
    private String imageBase64;

}
