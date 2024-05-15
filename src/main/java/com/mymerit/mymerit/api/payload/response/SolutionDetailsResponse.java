package com.mymerit.mymerit.api.payload.response;

import com.mymerit.mymerit.domain.entity.User;
import com.mymerit.mymerit.domain.models.ProgrammingLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SolutionDetailsResponse {
    private User user;
    private ProgrammingLanguage language;
    private List<TestResponse> testResults;
    private List<GridFileResponse> files;
    private Boolean isAlreadyRated;
}
