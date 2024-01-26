package com.mymerit.mymerit.domain.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignInRequest {
    @NotBlank
    private String username;
    private String password;
}
