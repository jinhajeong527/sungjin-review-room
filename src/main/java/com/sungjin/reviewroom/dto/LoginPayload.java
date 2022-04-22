package com.sungjin.reviewroom.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginPayload {
    @NotBlank
    private String email;
	@NotBlank
	private String password;
}
