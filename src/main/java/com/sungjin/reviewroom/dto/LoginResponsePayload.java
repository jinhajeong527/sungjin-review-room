package com.sungjin.reviewroom.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponsePayload {
    private int id;
    private String username;
    private String email;
    private List<String> roles;
    String message;
    String verificationToken;
    
    public LoginResponsePayload(String message, String verificationToken) {
        this.message = message;
        this.verificationToken = verificationToken;
    }
    public LoginResponsePayload(int id, String username, String email, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
    
    

}
