package com.blog.recovery.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

public class EmailPasswordAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper mapper;

    public EmailPasswordAuthFilter(String url,ObjectMapper mapper) {
        super(url);
        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        EmailPassword emailPassword
                = mapper.readValue(request.getInputStream(), EmailPassword.class);

        UsernamePasswordAuthenticationToken authToken
                = UsernamePasswordAuthenticationToken.unauthenticated(
                        emailPassword.email,
                        emailPassword.password
            );

        authToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authToken);
    }

    @Getter
    public static class EmailPassword{

        private String email;
        private String password;
    }
}
