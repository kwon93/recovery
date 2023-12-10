package com.blog.recovery.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = RecoveryMockSecurityContext.class)
public @interface RecoveryMockUser {

    String email() default "kwon93@naver.com";
    String name() default "kwon";
    String role() default "ROLE_ADMIN";
    String password() default "k1234";

}
