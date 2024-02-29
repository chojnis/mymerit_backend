package com.mymerit.mymerit.api.payload.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtResponse {
    private String token;
    private String type = "Bearer";

    public JwtResponse(String token) {
        this.token = token;
    }
}