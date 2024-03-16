package com.mymerit.mymerit.domain.programexecution;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ExecuteProgramResult {

    @NotBlank
    private String compileOutput;

}
