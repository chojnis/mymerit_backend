package com.mymerit.mymerit.domain.programexecution;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import static com.mymerit.mymerit.domain.programexecution.ExecuteProgramRequest.ProgramType.MULTI_FILE;
import static com.mymerit.mymerit.domain.programexecution.ExecuteProgramRequest.ProgramType.SINGLE_FILE;

@Setter
@Getter
@AllArgsConstructor
public class ExecuteProgramRequest {

    @NotBlank
    ProgrammingLanguage programmingLanguage;

    @NotBlank
    Set<String> programFiles;

    public ProgramType getProgramType() {
        return programFiles.size() > 1 ? MULTI_FILE : SINGLE_FILE;
    }

    public enum ProgrammingLanguage {

        JAVA,
        C,

        CPP

    }

    public enum ProgramType {

        SINGLE_FILE,

        MULTI_FILE

    }

}
