package com.mymerit.mymerit.api.payload.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ProgramExecutionApiResponse {

    @NotBlank
    String compileOutput;

}
