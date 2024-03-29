package com.sungjin.reviewroom.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.sungjin.reviewroom.dao.GenreRepository;
import com.sungjin.reviewroom.dao.RoleRepository;
import com.sungjin.reviewroom.dto.LoginPayload;
import com.sungjin.reviewroom.dto.LoginResponsePayload;
import com.sungjin.reviewroom.dto.MessageResponse;
import com.sungjin.reviewroom.dto.SignupPayload;
import com.sungjin.reviewroom.entity.Reviewer;
import com.sungjin.reviewroom.entity.VerificationToken;
import com.sungjin.reviewroom.event.OnSignupCompleteEvent;
import com.sungjin.reviewroom.exception.ReviewerAlreadyExistException;
import com.sungjin.reviewroom.security.JwtUtils;
import com.sungjin.reviewroom.security.UserDetailsImpl;
import com.sungjin.reviewroom.service.RefreshTokenService;
import com.sungjin.reviewroom.service.ReviewerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "${spring.data.rest.base-path}/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    ReviewerService reviewerService;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    RoleRepository roleRepository; 
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    private MessageSource messages;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Value("${spring.data.rest.base-path}")
    String basePath; 

    @PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginPayload loginPayload, HttpServletResponse httpServletResponse) {
        Authentication authentication = null;
		try {
            /* 
            UsernamePasswordAuthenticationToken : 
            An Authentication implementation that is designed for simple presentation of a username and password.
            로그인 리퀘스트로부터 { username, password }를 얻어오며, AuthenticationManager 는 로그인 어카운트를 인증하기 위해서 사용할 것이다.
            */
            authentication = authenticationManager.authenticate(
				             new UsernamePasswordAuthenticationToken(loginPayload.getEmail(), loginPayload.getPassword())
                             );
        } catch(DisabledException exception) {
            // 아직 회원가입 후 이메일 인증하지 않아서 verified 되지 않은 리뷰어가 로그인 시도 했을 경우(Reviewer 엔티티의 verified가 false일 것이다.)
            String email = loginPayload.getEmail();
            String token = reviewerService.findTokenWithLoginInfo(email);
            LoginResponsePayload loginResponsePayload = new LoginResponsePayload("This reviewer is not verified yet", token);
            return ResponseEntity.badRequest().body(loginResponsePayload);
        }

        // Authentication 오브젝트 사용하여 Security Context 업데이트 한다.
		SecurityContextHolder.getContext().setAuthentication(authentication);
       
        // Principle: 현재 검증된 유저(authenticated user)를 뜻한다.
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        ResponseCookie refreshTokenCookie = refreshTokenService.generateRefreshTokenCookie(userDetails);

		List<String> roles = userDetails.getAuthorities().stream()
				                        .map(item -> item.getAuthority())
				                        .collect(Collectors.toList());

        
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                  .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                                  .body(new LoginResponsePayload(
											userDetails.getId(), 
											userDetails.getUsername(), 
											userDetails.getEmail(), 
											roles));
	}
    // 리뷰어 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupPayload signupPayload, HttpServletRequest request) {
        try { // Reviewer 등록 시도
            Reviewer reviewerWhoJustSignedUp = reviewerService.registerUser(signupPayload);
            // 이벤트의 발행
            String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + basePath;
            eventPublisher.publishEvent(new OnSignupCompleteEvent(reviewerWhoJustSignedUp, request.getLocale(), appUrl));
        } catch(ReviewerAlreadyExistException exception) {  // 존재하는 Reviewer일 경우
             return ResponseEntity
			 		.badRequest()
			 		.body(new MessageResponse("Error: Email is already in use!"));
        } catch(RuntimeException exception) {
             return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error has occured while sending verification email: " + exception.getMessage()));
        }
        
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();

        return ResponseEntity.ok()
                             .header(HttpHeaders.SET_COOKIE, cookie.toString())
                             .body(new MessageResponse("You've been signed out!"));
    } 

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmSignup(WebRequest request, @RequestParam("token") String token) {
        // 생성된 인증토큰 얻어오기
        VerificationToken verificationToken = reviewerService.getVerificationToken(token);

        if (verificationToken == null) {
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Invalid Verification Code"));
        }
        // 해당 verificationToken 과 일대일 관계 갖는 Reviewer Entity 얻어온다.
        // VerificationToken => Reviewer 단방향 관계.
        Reviewer reviewer = verificationToken.getReviewer();

        Calendar cal = Calendar.getInstance();
        // 24시간 지났을 경우 인증 토큰 만료 로직
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Verification Code Has Been Expired"));
        } 
        // 시간 만료 전 인증 링크 눌렀을 경우에 verified true로 set 해주고, 다시 저장한다.
        reviewer.setVerified(true); 
        reviewerService.saveSignedUpReviewer(reviewer);
        return ResponseEntity.ok(new MessageResponse("Reviewer has been successfully verified!"));
    }

    //인증 토큰 만료 유저 토큰 재생성 시도
    @GetMapping("/resendToken")
    public ResponseEntity<?> resendVerificationToken(HttpServletRequest request, @RequestParam("token") String existingToken) {
        VerificationToken newToken = reviewerService.generateVerificationTokenAgain(existingToken);
        Reviewer reviewerWhoRequestNewToken = reviewerService.getReviewer(newToken.getToken());
        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + basePath;
        SimpleMailMessage email = constructResendVerificationTokenEmail(appUrl, request.getLocale(), newToken, reviewerWhoRequestNewToken);
        mailSender.send(email);
        return ResponseEntity.ok("Successfully resent the verification email");
    }

    private SimpleMailMessage constructResendVerificationTokenEmail(String contextPath, Locale locale, VerificationToken newToken, Reviewer reviewer) {
        String confirmationUrl = contextPath + "/auth/confirm?token=" + newToken.getToken();
        
        String message = messages.getMessage("message.resendToken", null, locale);
        message += System.lineSeparator();
        message += confirmationUrl;
        
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(reviewer.getFirstName() + messages.getMessage("message.resendTokenSubject", null, locale));
        email.setText(message);
        email.setTo(reviewer.getEmail());
        return email;
    }
}
