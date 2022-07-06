package com.sungjin.reviewroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponsePayload {
    String message;
    String token;
}
