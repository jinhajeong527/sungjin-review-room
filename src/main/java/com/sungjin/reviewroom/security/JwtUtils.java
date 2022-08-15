package com.sungjin.reviewroom.security;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;

import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.sungjin.reviewroom.dao.RefreshTokenRepository;
import com.sungjin.reviewroom.dao.ReviewerRepository;

import com.sungjin.reviewroom.service.RefreshTokenService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${sungjin.reviewroom.app.jwtSecret}")
    private String jwtSecret;
    @Value("${sungjin.reviewroom.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${sungjin.reviewroom.app.jwtCookieName}")
    private String jwtCookie;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    ReviewerRepository reviewerRepository;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
    // generateJwtCookie @Param : UserDetailsImpl userDetails
    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails) {
        String jwt = generateJwtToken(userDetails.getEmail());
        ResponseCookie cookie = ResponseCookie
                                    .from(jwtCookie, jwt)
                                    .maxAge(jwtExpirationMs)
                                    .httpOnly(true)
                                    .build();
        return cookie;
    }
    // generateJwtCookie @Param : String email
    public ResponseCookie generateJwtCookie(String email) {
        String jwt = generateJwtToken(email);
        ResponseCookie cookie = ResponseCookie
                                    .from(jwtCookie, jwt)
                                    .maxAge(jwtExpirationMs)
                                    .httpOnly(true)
                                    .build();
        return cookie;
    }

    // 로그아웃 : jwt 토큰 값을 null로 바꿔준다.
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie
                                    .from(jwtCookie, null)
                                    .build();
        return cookie;
    }
    // jwt 생성
    public String generateJwtToken(String email) {    
        return Jwts.builder()
                   .setSubject(email)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                   .signWith(SignatureAlgorithm.HS512, jwtSecret)
                   .compact();
    }
    // jwt 토큰 정보에서 리뷰어 이메일 정보 얻어오기
    public String getReviewerEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
    }
}
