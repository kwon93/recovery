package com.blog.recovery.controller;

import com.blog.recovery.request.LoginDTO;
import com.blog.recovery.response.LoginResponse;
import com.blog.recovery.response.SessionResponse;
import com.blog.recovery.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO request) {
        String accessToken = userService.signIn(request);

        ResponseCookie cookie = getResponseCookie(accessToken);

        log.info(">>>>> Cookie =  {}", cookie.toString());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .build();
    }

    private ResponseCookie getResponseCookie(String accessToken) {

        return ResponseCookie.from("SESSION", accessToken) // todo 서버환경에 따른 분리 필요
                .path("/")
                .domain("localhost")
                .httpOnly(false)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();
    }
}
