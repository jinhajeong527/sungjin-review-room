package com.sungjin.reviewroom.security;

import java.io.IOException;
//import java.util.Arrays;
//import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
//import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


@Description("Performs Request Filtering")
public class AuthTokenFilter extends OncePerRequestFilter {
    /*  JwtUtils 목적 및 용도
     1. username, date, expiration, secret을 통해 JWT 생성한다.
     2. JWT에서 email을 얻어온다.
     3. JWT 검증한다.
    */
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            //JWT를 Authorization header에서 얻어온다(Bearer prefix 지우기)
            String jwt = parseJwt(request);
           
            //해당 request가 JWT를 갖고 있으면, validate하고, email 파싱한다.
            if(jwt != null && jwtUtils.validateJwtToken(jwt)) {
               
                String email = jwtUtils.getUserEmailFromJwtToken(jwt);
             
                //email 정보를 통해서 UserDetails 얻어와 Authentication 오브젝트 생성한다.
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //현재의 UserDetails를 SecurityContext에 세팅한다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
                /*
                이 이후에는 UserDetails 얻어오길 원할 때마다 SecurityContext를 아래와 같이 이용한다.
                UserDetails userDetails =
	                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                */
            }
        } catch(Exception e) {
            logger.error("Cannot set reviewer authentiation: {}", e);
        }
        filterChain.doFilter(request, response);
        
    }
    private String parseJwt(HttpServletRequest request) {


        /* 
        client에서 요청 보낼 때마다 쿠키로 넣어주는 것이라면 아래의 코드로 수정되어야 할 것이다.
        Optional<String> value = Arrays.stream(request.getCookies())
                             .filter(c -> "jwt".equals(c.getName()))
                             .map(Cookie::getValue).findAny();
         String valueStr = value.get();
         return valueStr;
        */
        String headerAuth = request.getHeader("Authorization");
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            System.out.println(headerAuth);
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
    
}
