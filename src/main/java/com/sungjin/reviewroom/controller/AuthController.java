package com.sungjin.reviewroom.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.sungjin.reviewroom.dao.ReviewerRepository;
import com.sungjin.reviewroom.dao.RoleRepository;
import com.sungjin.reviewroom.dto.JwtResponsePayload;
import com.sungjin.reviewroom.dto.LoginPayload;
import com.sungjin.reviewroom.dto.MessageResponse;
import com.sungjin.reviewroom.dto.SignupPayload;
import com.sungjin.reviewroom.entity.Reviewer;
import com.sungjin.reviewroom.entity.Role;
import com.sungjin.reviewroom.model.EnumRole;
import com.sungjin.reviewroom.security.JwtUtils;
import com.sungjin.reviewroom.security.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
    //ROLE REVIEWER 있는 사람만 접근 가능한지 확인하기 위한 테스트 REST API
    @GetMapping("/user")
    @PreAuthorize("hasRole('REVIEWER')")
	public String userAccess() {
		return "User Content.";
	}

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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupPayload signupPayload) {
        if (reviewerRepository.existsByEmail(signupPayload.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
        Reviewer reviewer = new Reviewer(signupPayload.getEmail(), 
                                         encoder.encode(signupPayload.getPassword()), 
                                         signupPayload.getFirstName(), 
                                         signupPayload.getLastName(), 
                                         signupPayload.getMbti()
                                        );
        Set<String> rolesStr = signupPayload.getRole();
        Set<Role> roles = new HashSet<>();
        if(rolesStr == null) {
            Role userRole = roleRepository.findByName(EnumRole.ROLE_REVIEWER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            rolesStr.forEach(role -> {
                switch(role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(EnumRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                    break;
                default:
                    Role userRole = roleRepository.findByName(EnumRole.ROLE_REVIEWER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }
        reviewer.setRoles(roles);
        reviewerRepository.save(reviewer);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    } 
    
}
