package com.blog.recovery.controller;

import com.blog.recovery.domain.Session;
import com.blog.recovery.domain.Users;
import com.blog.recovery.exception.InvalidSignInfomation;
import com.blog.recovery.repository.SessionRepository;
import com.blog.recovery.repository.UserRepository;
import com.blog.recovery.request.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SessionRepository sessionRepository;

    @BeforeEach
    public void beforeEach() {
        sessionRepository.deleteAll();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자 로그인요청과 DB 사용자 정보가 일치해 로그인 성공시 http 응답 200코드로 성공해야한다.")
    void test() throws Exception {
        //given
        final String email = "kwon@naver.com";
        final String password = "k1234";

        Users usersInfo = Users.builder()
                .email(email)
                .password(password)
                .build();

        userRepository.save(usersInfo);

        LoginDTO loginRequest = LoginDTO.builder()
                .email(email)
                .password(password)
                .build();


        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("사용자 로그인정보와 DB 사용자 정보 불일치시 400코드와 InvalidSignInfomation Exception을 발생시켜야한다.")
    void test2() throws Exception {
        //given
        final String email = "kwon@naver.com";
        final String password = "k1234";
        final String invalidPW = "kdh93";

        Users usersInfo = Users.builder()
                .email(email)
                .password(password)
                .build();

        userRepository.save(usersInfo);

        LoginDTO loginRequest = LoginDTO.builder()
                .email(email)
                .password(invalidPW)
                .build();


        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("이메일과 비밀번호를 다시 확인해주세요."))
                .andDo(MockMvcResultHandlers.print());

    }



    @Test
    @DisplayName("로그인 성공 후 세션 응답")
    void test3() throws Exception {
        //given
        final String email = "kwon@naver.com";
        final String password = "k1234";

        Users usersInfo = Users.builder()
                .email(email)
                .password(password)
                .build();

        userRepository.save(usersInfo);

        LoginDTO loginRequest = LoginDTO.builder()
                .email(email)
                .password(password)
                .build();


        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().stringValues("SESSION", Matchers.notNullValue()))
                .andDo(MockMvcResultHandlers.print());



    }


    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지에 접속에 성공한다.")
    void test4() throws Exception {
        //given
        final String email = "kwon@naver.com";
        final String password = "k1234";

        Users user = Users.builder()
                .email(email)
                .password(password)
                .build();

        Session session = user.addSession();

        userRepository.save(user);

        LoginDTO loginRequest = LoginDTO.builder()
                .email(email)
                .password(password)
                .build();

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse();


        Cookie[] cookies = response.getCookies();

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                    .cookie(cookies)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    @DisplayName("로그인 후 검증되지않은 세션값으로 권한이 필요한 페이지에 접속 할 수 없다. : 401 HTTP Status code 발생")
    void test5() throws Exception {
        //given
        final String email = "kwon@naver.com";
        final String password = "k1234";

        Users user = Users.builder()
                .email(email)
                .password(password)
                .build();

        Session session = user.addSession();

        userRepository.save(user);

        LoginDTO loginRequest = LoginDTO.builder()
                .email(email)
                .password(password)
                .build();

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse();

        Cookie[] cookies = response.getCookies();
        cookies[0].setValue("InvalidToken");


        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                        .cookie(cookies)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());

    }
}