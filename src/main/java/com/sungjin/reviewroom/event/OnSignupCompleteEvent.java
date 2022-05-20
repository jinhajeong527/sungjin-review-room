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
        super(reviewer);
        this.reviewer = reviewer;
        this.locale = locale;
        this.appUrl = appUrl;
    }
    
}
