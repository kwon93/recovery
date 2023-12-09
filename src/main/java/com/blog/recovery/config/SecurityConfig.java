package com.blog.recovery.config;

import com.blog.recovery.domain.Users;
import com.blog.recovery.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {


    /** @EnableWebSecurity를 사용하고있을때
     WebSecurityCustomizer 사용시 WebSecurity라는 객체를 스프링이 자동으로 주입해준다.
     **/
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers(
                        new AntPathRequestMatcher("/favicon.ico"),
                        new AntPathRequestMatcher("/error"),
                        toH2Console() // H2 DB
                );
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        return http
                .authorizeHttpRequests(request ->
                        request.requestMatchers(new AntPathRequestMatcher("/auth/login")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/auth/signUp")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/user")).hasAnyRole("USER","ADMIN")
                                .requestMatchers(new AntPathRequestMatcher("/admin"))
                                    .access(new WebExpressionAuthorizationManager("hasRole('ADMIN') AND hasAuthority('WRITE')"))
                                                                                    //역할 : Role , 권한 : Authority
                                .anyRequest().authenticated())
                .formLogin(request -> {
                    request.usernameParameter("userName")
                            .passwordParameter("password")
                            .loginPage("/auth/login")
                            .loginProcessingUrl("/auth/login")
                            .defaultSuccessUrl("/");
                })
                .rememberMe(rm ->
                        rm.rememberMeParameter("remember")
                        .alwaysRemember(false)
                        .tokenValiditySeconds(2592000)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {

        return username -> {
                Users user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username + " 을(를) 찾을 수 없습니다."));
                return new UserPrincipal(user);
        };

//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        UserDetails user = User
//                .withUsername("kwon93")
//                .password(passwordEncoder().encode("1234"))
//                .roles("ADMIN")
//                .build();
//
//        manager.createUser(user);
//
//        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
