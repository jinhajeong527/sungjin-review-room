package com.sungjin.reviewroom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/")
public class SungjinReviewRoomController {
    // 메인 페이지
    @GetMapping
    public String main() {
        return "index.html";
    }
    
    // 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "/login/login.html";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signup() {
        return "/signup/signup.html";
    }
}
