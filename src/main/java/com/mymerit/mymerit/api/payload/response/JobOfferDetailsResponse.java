package com.mymerit.mymerit.api.payload.response;

import com.mymerit.mymerit.domain.entity.Company;
import com.mymerit.mymerit.domain.entity.Task;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class JobOfferDetailsResponse {
    @NotNull
    private String id;

    @NotBlank
    private String jobTitle;

    @NotBlank
    private String description;

    @NotEmpty
    private List<String> requiredSkills;

    @NotEmpty
    private List<String> preferredSkills;

    @NotEmpty
    private List<String> workLocations;

    @NotEmpty
    private List<String> technologies;

    @NotNull
    @Valid
    private Company company;

    private Task task; //w zaleznosci od isOpen : null lub Task

    @NotEmpty
    private Integer salary;
}
