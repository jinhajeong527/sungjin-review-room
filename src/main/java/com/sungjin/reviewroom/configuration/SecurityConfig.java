package com.sungjin.reviewroom.configuration;



import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity //Spring이 해당 클래스 global Web Security로 사용할 수 있도록 함.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
  
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       
    }
    /*
    스프링에 CORS 및 CSRF 설정 어떻게 할 것인지 말하는 역할.
    모든 유저가 authenticated 되기를 원하거나, 어떤 필터 사용되기를 원하거나,
    언제 작동하길 원하는지를 설정해줄 때나, 어떤 예외 핸들러가 선택되기를 원하는 지 등 지정할 때 
    */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        
    }
    
}
