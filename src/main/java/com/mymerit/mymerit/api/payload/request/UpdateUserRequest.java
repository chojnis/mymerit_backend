package com.mymerit.mymerit.api.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserRequest {
    private String imageBase64;
    private String description;
    private String password;
    private String username;
    //
    private String socialName1;
    private String socialLink1;
    private String socialName2;
    private String socialLink2;
    private String socialName3;
    private String socialLink3;
}