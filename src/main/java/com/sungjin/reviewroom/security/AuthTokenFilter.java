package com.sungjin.reviewroom.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sungjin.reviewroom.dto.MessageResponse;
import com.sungjin.reviewroom.entity.RefreshToken;
import com.sungjin.reviewroom.exception.TokenRefreshException;
import com.sungjin.reviewroom.service.RefreshTokenService;

import io.jsonwebtoken.ExpiredJwtException;


import java.util.List;
import java.util.Optional;
import java.util.Arrays;


@Description("Performs Request Filtering")
public class AuthTokenFilter extends OncePerRequestFilter {
    /*  JwtUtils 목적 및 용도
     *  1. username, date, expiration, secret을 통해 JWT 생성한다.
     *  2. JWT에서 email을 얻어온다.
     *  3. JWT 검증한다.
    */
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;
    
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    
    private static final List<String> EXCLUDE_URL =
            Arrays.asList(
                "/api/genre","/api/show/mostReviewed","/login","/signup","/api/auth", "/reauth",
                "/css/", "/js/",".css",".js", ".ico", ".png",".jpeg",".jpg"
            );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            String jwt = parseJwt(request);
            // 해당 request가 JWT를 갖고 있으면, validate하고, email 파싱한다.
            if(jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String email = jwtUtils.getReviewerEmailFromJwtToken(jwt);
                // email 정보를 통해서 UserDetails 얻어와 Authentication 오브젝트 생성한다.
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 현재의 UserDetails를 SecurityContext에 세팅한다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
                /*
                이 이후에는 UserDetails 얻어오길 원할 때마다 SecurityContext를 아래와 같이 이용한다.
                UserDetails userDetails =
	                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                */
            }
        }
        catch(ExpiredJwtException e) {
            String refreshTokenValue = refreshTokenService.getRefreshTokenFromCookies(request);
            Optional<RefreshToken> refreshTokenOptional = refreshTokenService.findByToken(refreshTokenValue);
            if(refreshTokenOptional.isPresent()) { // 데이터베이스에 리프레쉬 토큰이 존재하는지 확인한다.
                try { // 리프레쉬 토큰 또한 만료되었는지 확인한다.
                    RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenOptional.get());
                    String email = refreshToken.getReviewer().getEmail();
                    ResponseCookie refreshedJwtCookie = jwtUtils.generateJwtCookie(email);
                    response.setHeader("Set-Cookie", refreshedJwtCookie.toString());
                } catch(TokenRefreshException exception) {
                    // 리프레쉬 토큰 또한 만료되었을 경우 jwtToken 값 비워준다.
                    ResponseCookie cleanCookie = jwtUtils.getCleanJwtCookie();
                    // 클라이언트에게 리뷰어가 다시 로그인해야 함을 알린다.
                    MessageResponse messageResponse = new MessageResponse("Refresh token was expired. Please make a new signin request");
                    response.setHeader("Set-Cookie", cleanCookie.toString());
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    response.getWriter().write(convertObjectToJson(messageResponse));
                }   
            }
        }
        catch(Exception e) {
            logger.error("Cannot set reviewer authentiation: {}", e);
        }
        filterChain.doFilter(request, response);
        
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        for(String url : EXCLUDE_URL) {
            if(request.getRequestURL().toString().contains(url)) {
                return true;
            }   
        }
       return false;
    }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        return jwt;
        /* 
            JWT를 Authorization header에서 얻어오는 방식으로 할 경우에
            String headerAuth = request.getHeader("Authorization");
            if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
                return headerAuth.substring(7, headerAuth.length());
            }
            return null;
        */
    }
    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
