package com.sungjin.reviewroom.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthTokenFilter extends OncePerRequestFilter {

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
        String headerAuth = request.getHeader("Authorization");
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            System.out.println(headerAuth);
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
    
}
