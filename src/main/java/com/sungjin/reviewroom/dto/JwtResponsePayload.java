package com.sungjin.reviewroom.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class JwtResponsePayload {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private int id;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponsePayload(String token, String refreshToken, int id, String username, String email, List<String> roles) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }


    
}
