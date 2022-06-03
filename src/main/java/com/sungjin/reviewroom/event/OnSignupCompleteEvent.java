package com.sungjin.reviewroom.event;

import java.util.Locale;

import com.sungjin.reviewroom.entity.Reviewer;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnSignupCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private Reviewer reviewer;

    public OnSignupCompleteEvent(Reviewer reviewer, Locale locale, String appUrl) {
        
        super(reviewer); //ApplicationEvent에 인자 하나만 넣는 생성자가 의미하는 것이 무엇인지 알기

        this.reviewer = reviewer;
        this.locale = locale;
        this.appUrl = appUrl;
    }
    
}
