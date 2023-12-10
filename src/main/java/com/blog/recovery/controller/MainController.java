package com.blog.recovery.controller;

import com.blog.recovery.config.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {


    @GetMapping("/")
    public String main(){
        return "메인 페이지입니다.";
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal UserPrincipal userPrincipal){

        return userPrincipal.getUsername()+"님의 페이지입니다.";
    }

    @GetMapping("/admin")
    public String admin(){
        return "관리자 페이지입니다.";
    }


}
