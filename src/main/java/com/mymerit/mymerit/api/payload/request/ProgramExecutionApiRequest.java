package com.mymerit.mymerit.api.payload.request;

import com.mymerit.mymerit.domain.programexecution.ExecuteProgramRequest;
import com.mymerit.mymerit.domain.programexecution.ExecuteProgramRequest.ProgrammingLanguage;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class ProgramExecutionApiRequest {

    @NotBlank
    String fileName;

    @NotBlank
    String fileContent;

    public ExecuteProgramRequest toDomain() {
        return new ExecuteProgramRequest(
                ProgrammingLanguage.valueOf(this.fileName.substring(this.fileName.lastIndexOf("." + 1))),
                Set.of(this.fileContent)
        );
    }

}
