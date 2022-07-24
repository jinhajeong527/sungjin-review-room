package com.sungjin.reviewroom.service;

import java.util.Optional;

import com.sungjin.reviewroom.entity.RefreshToken;

public interface RefreshTokenService {
    public Optional<RefreshToken> findByToken(String token);
    public RefreshToken createRefreshToken(int userId);
    public RefreshToken verifyExpiration(RefreshToken token);
    public int deleteByReviewerId(int userId);
}
