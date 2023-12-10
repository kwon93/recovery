package com.blog.recovery.config;

import com.blog.recovery.domain.Users;
import com.blog.recovery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class RecoveryMockSecurityContext implements WithSecurityContextFactory<RecoveryMockUser> {


    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(RecoveryMockUser annotation) {
        Users user = Users.builder()
                .email(annotation.email())
                .name(annotation.name())
                .password(annotation.password())
                .build();

        userRepository.save(user);

        UserPrincipal userPrincipal = new UserPrincipal(user);

        var role = new SimpleGrantedAuthority("ROLE_ADMIN");
        var token = new UsernamePasswordAuthenticationToken(userPrincipal, user.getPassword(), List.of(role));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(token);

        return securityContext;
    }
}
