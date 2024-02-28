package com.mymerit.mymerit.api.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignInRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}