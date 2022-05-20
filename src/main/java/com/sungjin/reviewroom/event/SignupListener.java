package com.sungjin.reviewroom.event;

import java.util.UUID;

import com.sungjin.reviewroom.entity.Reviewer;
import com.sungjin.reviewroom.service.ReviewerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
//import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SignupListener implements ApplicationListener<OnSignupCompleteEvent> {

    @Autowired
    private ReviewerService service;
 
    //@Autowired
    //private MessageSource messages;
 
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnSignupCompleteEvent event) {
        this.confirmSignup(event);
    }

    private void confirmSignup(OnSignupCompleteEvent event) {
        Reviewer reviewer = event.getReviewer();
        String token = UUID.randomUUID().toString();
        service.createVerificationTokenForReviewer(reviewer, token);
      
        String recipientAddress = reviewer.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
          = "/api/auth/confirm?token=" + token;
          //event.getAppUrl() 
        //String message = messages.getMessage("message.regSucc", null, event.getLocale());
        
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("\r\n" + "http://localhost:8080" + confirmationUrl);
        mailSender.send(email);
    }
}
