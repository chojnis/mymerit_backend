package com.mymerit.mymerit.api.payload.response;

import com.mymerit.mymerit.domain.entity.Company;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class JobOfferListResponse {
    @NotNull
    private String id;

    @NotBlank
    private String jobTitle;

    @NotEmpty
    private List<String> workLocations;

    @NotEmpty
    private List<String> technologies;

    @NotNull
    private Integer reward;

    @NotNull
    private LocalDateTime opensAt;

    @NotNull
    private LocalDateTime closesAt;

    @NotNull
    @Valid
    private Company company;

    @NotNull
    private Integer salary;


}
