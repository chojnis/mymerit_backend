package com.mymerit.mymerit.api.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class JudgeTokenRequest implements Serializable {

    @NotBlank
    private String token;
    @NotBlank
    private String error;

    public JudgeTokenRequest(String token, String error) {
        this.token = token;
        this.error = error;
    }
}
