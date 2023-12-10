package com.blog.recovery.config;

import com.blog.recovery.config.filter.EmailPasswordAuthFilter;
import com.blog.recovery.config.handler.Http401Handler;
import com.blog.recovery.config.handler.Http403Handler;
import com.blog.recovery.config.handler.LoginFailHandler;
import com.blog.recovery.domain.Users;
import com.blog.recovery.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper mapper;
    private final UserRepository userRepository;

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
                            .defaultSuccessUrl("/")
                            .failureHandler(new LoginFailHandler(mapper));
                })
                .addFilterBefore(emailPasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> {
                            e.accessDeniedHandler(new Http403Handler(mapper));
                            e.authenticationEntryPoint(new Http401Handler(mapper));
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
    public EmailPasswordAuthFilter emailPasswordAuthFilter(){
        EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/auth/login", mapper);

        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
        filter.setAuthenticationFailureHandler(new LoginFailHandler(mapper));
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository()); //세션 발급

        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setValiditySeconds(3600 * 24 * 30);

        filter.setRememberMeServices(rememberMeServices);

        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService(userRepository));
        provider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(provider);
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
