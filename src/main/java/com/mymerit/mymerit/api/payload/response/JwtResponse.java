package com.mymerit.mymerit.api.payload.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private String email;

    public JwtResponse(String token, String id, String username, String email) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
    }
}