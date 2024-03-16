package com.mymerit.mymerit.domain.programexecution;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ProgramExecutionId {

    @NotBlank
    String value;

    public static ProgramExecutionId from(String value) {
        return new ProgramExecutionId(value);
    }

}
