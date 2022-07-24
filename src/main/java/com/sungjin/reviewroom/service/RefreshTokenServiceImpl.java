package com.sungjin.reviewroom.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sungjin.reviewroom.dao.RefreshTokenRepository;
import com.sungjin.reviewroom.dao.ReviewerRepository;
import com.sungjin.reviewroom.entity.RefreshToken;
import com.sungjin.reviewroom.exception.TokenRefreshException;
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${sungjin.reviewroom.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private ReviewerRepository reviewerRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(int userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setReviewer(reviewerRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
    // 리프레쉬 토큰이 만료되었는지 체크하고, 만료되었을 경우 데이터베이스에서 삭제하고 TokenRefreshException 던진다.
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Override
    @Transactional
    public int deleteByReviewerId(int userId) {
        return refreshTokenRepository.deleteByReviewer(reviewerRepository.findById(userId).get());
    }
    
}
