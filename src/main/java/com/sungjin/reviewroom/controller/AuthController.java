package com.sungjin.reviewroom.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.sungjin.reviewroom.dao.ReviewerRepository;
import com.sungjin.reviewroom.dao.RoleRepository;
import com.sungjin.reviewroom.dto.JwtResponsePayload;
import com.sungjin.reviewroom.dto.LoginPayload;
import com.sungjin.reviewroom.security.JwtUtils;
import com.sungjin.reviewroom.security.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager; 
    @Autowired
    ReviewerRepository reviewerRepository;
    @Autowired
    RoleRepository roleRepository; 
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginPayload loginPayload) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginPayload.getEmail(), loginPayload.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
       
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
               
		return ResponseEntity.ok(new JwtResponsePayload(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}
    
}
