package com.sungjin.reviewroom.service;

import java.util.Optional;

import org.springframework.http.ResponseCookie;

import com.sungjin.reviewroom.entity.RefreshToken;
import com.sungjin.reviewroom.security.UserDetailsImpl;

public interface RefreshTokenService {
    public Optional<RefreshToken> findByToken(String token);
    public ResponseCookie generateRefreshTokenCookie(UserDetailsImpl userDetails);
    public RefreshToken createRefreshToken(int userId);
    public RefreshToken verifyExpiration(RefreshToken token);
    public int deleteByReviewerId(int userId);
}
