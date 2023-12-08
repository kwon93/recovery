package com.blog.recovery.controller;

import com.blog.recovery.config.AppConfig;
import com.blog.recovery.request.LoginDTO;
import com.blog.recovery.response.LoginResponse;
import com.blog.recovery.response.SessionResponse;
import com.blog.recovery.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AppConfig appConfig;

    @PostMapping("/jwt/login")
    public SessionResponse jwtLogin(@RequestBody LoginDTO request){
        Long userId = userService.signIn(request);

        Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        //JWT Key String으로 뽑아내기
//        byte[] encodedKey = secretKey.getEncoded();
//        String strkey = Base64.getEncoder().encodeToString(encodedKey);


        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(appConfig.getKEY()));

        String jws = Jwts.builder().setSubject(String.valueOf(userId)).signWith(key).compact();

        return new SessionResponse(jws);
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
