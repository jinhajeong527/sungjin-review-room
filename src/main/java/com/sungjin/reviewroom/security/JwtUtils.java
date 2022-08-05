package com.sungjin.reviewroom.security;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

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
    @Value("${spring.data.rest.base-path}")
    String appUrl; 

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails) {
        String jwt = generateJwtToken(userDetails.getEmail());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path(appUrl).maxAge(jwtExpirationMs).httpOnly(true).build();
        return cookie;
    }
    // 로그아웃
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path(appUrl).build();
        return cookie;
    }

   
    public String generateJwtToken(String email) {    
        return Jwts.builder()
                   .setSubject((email))
                   .setIssuedAt(new Date())
                   .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                   .signWith(SignatureAlgorithm.HS512, jwtSecret)
                   .compact();
    }

    public String getUserEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
    }
    
}
