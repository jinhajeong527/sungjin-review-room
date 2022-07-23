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
        /* 
           OnSignupCompleteEvent는 ApplicationEvent를 상속 중이고, 
           ApplicationEvent는 EventObject를 상속 중이다.
           EventObject에서 해당 필드의 이름은 source이고, 이벤트가 최초 발생한 오브젝트를 의미한다.
           ApplicationEvent(Object source) 생성자에서 super(source);를 통해 부모 생성자를 호출하게 되고,
           이벤트가 처음 발생한 곳에 해당 오브젝트를 source 하게 된다. 
        */
        super(reviewer);

        this.reviewer = reviewer;
        this.locale = locale;
        this.appUrl = appUrl;
    }
    
}
