package com.sungjin.reviewroom.configuration;


import com.sungjin.reviewroom.security.AuthEntryPointJwt;
import com.sungjin.reviewroom.security.AuthTokenFilter;
import com.sungjin.reviewroom.security.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //Spring이 해당 클래스 Global Web Security로 사용할 수 있도록 함.
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //User를 username에 의해 로드하는 메서드(loadUserByUsername) 갖고 있고, UserDetails 리턴한다.
    @Autowired
	UserDetailsServiceImpl userDetailsService;

    //authentication 에러 잡아낸다.
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

    @Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    스프링에 CORS 및 CSRF 설정 어떻게 할 것인지 말하는 역할
    - 모든 유저가 authenticated 되기를 원하거나, 
    - 어떤 필터 사용되기를 원하거나(AuthTokenFilter),
    - 언제 작동하길 원하는지를 설정해줄 때나(UsernamePasswordAuthenticationFilter 전에 필터 작동), 
    - 어떤 예외 핸들러가 선택되기를 원하는 지(AuthEntryPointJwt) 등 지정할 때
    */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests().antMatchers("/api/auth/**").permitAll()
            .antMatchers("/api/test/**").permitAll()
            .anyRequest().authenticated();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    
}
