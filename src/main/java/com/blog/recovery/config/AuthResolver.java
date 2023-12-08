package com.blog.recovery.config;

import com.blog.recovery.config.data.UserSession;
import com.blog.recovery.controller.AuthController;
import com.blog.recovery.domain.Session;
import com.blog.recovery.exception.UnAutorizedException;
import com.blog.recovery.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Base64;

@RequiredArgsConstructor
@Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    private static final String KEY = "BTAQo1/fhbzvpOMCTBYjYtNuHgYGz3ubs+kya0+cmjE=";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jws = webRequest.getHeader("Authorization");

        if (jws == null || jws.equals("")){
            throw new UnAutorizedException();
        }

        byte[] decodedKey = Base64.getDecoder().decode(KEY);

        //jws 복호화 (검증하기위해)
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(decodedKey)
                    .build()
                    .parseClaimsJws(jws);

            String userId = claims.getBody().getSubject();

            return UserSession.builder()
                    .id(Long.parseLong(userId))
                    .build();

        } catch (JwtException e) {
            throw new UnAutorizedException();
        }

    }




}
