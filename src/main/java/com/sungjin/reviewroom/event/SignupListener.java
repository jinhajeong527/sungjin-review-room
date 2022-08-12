package com.sungjin.reviewroom.event;

import java.util.UUID;

import com.sungjin.reviewroom.entity.Reviewer;
import com.sungjin.reviewroom.service.ReviewerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SignupListener implements ApplicationListener<OnSignupCompleteEvent> {

    @Autowired
    private ReviewerService reviewerService;
 
    @Autowired
    private MessageSource messages;
 
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnSignupCompleteEvent event) {
        this.confirmSignup(event);
    }
    /* 
       OnSignupCompleteEvent이벤트를 받아서, 필요한 Reviewer 정보를 추출하고, Verification Token 생성한 후에 
       회원가입 컴펌 링크의 파라미터로 사용해 메일 보내 줄 메서드 
    */
    private void confirmSignup(OnSignupCompleteEvent event) {
        Reviewer reviewer = event.getReviewer();
        String token = UUID.randomUUID().toString();
        reviewerService.createVerificationTokenForReviewer(reviewer, token);
      
        String subject = reviewer.getFirstName() + 
            messages.getMessage("message.regSuccSubject", null, event.getLocale());
        String confirmationUrl = event.getAppUrl() + "/auth/confirm?token=" + token;

        String message = messages.getMessage("message.regSuccLink", null, event.getLocale());
        message += System.lineSeparator();
        message += confirmationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(message);
        email.setTo(reviewer.getEmail());
        mailSender.send(email);
    }
}
