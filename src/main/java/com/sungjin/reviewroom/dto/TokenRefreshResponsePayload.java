package com.sungjin.reviewroom.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshResponsePayload {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    public TokenRefreshResponsePayload(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }   
}
