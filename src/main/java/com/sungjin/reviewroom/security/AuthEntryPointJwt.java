package com.sungjin.reviewroom.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component //Authentication 관련 예외 핸들링 : 인증받지 않은 reviewer가 secured HTTP 리소스에 request 보낼 때 마다 트리거 된다. 
@Description("Handling Authentication Exception")
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException, DisabledException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        System.out.println(authException.getClass().getName());
        //HttpServletResponse.SC_UNAUTHORIZED는 401 에러이다. 해당 리퀘스트가 HTTP Authentication 요구함을 나타낸다. 
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
    
}
