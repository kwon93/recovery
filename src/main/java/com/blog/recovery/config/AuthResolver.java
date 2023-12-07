package com.blog.recovery.config;

import com.blog.recovery.config.data.UserSession;
import com.blog.recovery.domain.Session;
import com.blog.recovery.exception.UnAutorizedException;
import com.blog.recovery.repository.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        if (servletRequest == null){
            log.info("요청이 엄서용");
            throw new UnAutorizedException();
        }

        Cookie[] cookies = servletRequest.getCookies();

        if (cookies.length == 0){
            log.info("쿠키가 엄서용");
            throw new UnAutorizedException();
        }

        String token = cookies[0].getValue();

        Session session = sessionRepository.findByAccessToken(token)
                .orElseThrow(UnAutorizedException::new);

        return UserSession.builder()
                .id(session.getUser().getId())
                .build();
    }


}
