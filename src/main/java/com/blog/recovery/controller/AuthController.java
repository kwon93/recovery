package com.blog.recovery.controller;

import com.blog.recovery.config.AppConfig;
import com.blog.recovery.request.SignUp;
import com.blog.recovery.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @GetMapping("/auth/login")
    public String login(){
        return "로그인 페이지입니다.";
    }

    @PostMapping("/auth/signUp")
    public String  signUp(@RequestBody SignUp signUp){
        userService.signUp(signUp);
        return "회원가입완료";
    }



}
