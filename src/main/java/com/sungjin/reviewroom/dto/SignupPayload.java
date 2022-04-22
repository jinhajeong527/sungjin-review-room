package com.sungjin.reviewroom.dto;

import java.util.Set;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupPayload {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
    
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String mbti;

    private Set<String> role;
}
