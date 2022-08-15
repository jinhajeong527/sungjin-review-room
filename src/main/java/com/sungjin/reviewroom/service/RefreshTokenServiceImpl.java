package com.sungjin.reviewroom.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import com.sungjin.reviewroom.dao.RefreshTokenRepository;
import com.sungjin.reviewroom.dao.ReviewerRepository;
import com.sungjin.reviewroom.entity.RefreshToken;
import com.sungjin.reviewroom.exception.TokenRefreshException;
import com.sungjin.reviewroom.security.UserDetailsImpl;
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${sungjin.reviewroom.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    @Value("${sungjin.reviewroom.app.refreshTokenCookieName}")
    private String refreshTokenCookie;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private ReviewerRepository reviewerRepository;

    @Override
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, refreshTokenCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    @Override
    public ResponseCookie generateRefreshTokenCookie(UserDetailsImpl userDetails) {
        RefreshToken refreshToken = createRefreshToken(userDetails.getId());      
        ResponseCookie cookie = ResponseCookie
                                .from(refreshTokenCookie, refreshToken.getToken())
                                .maxAge(refreshTokenDurationMs)
                                .httpOnly(true)
                                .build();
        return cookie;
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
