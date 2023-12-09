package com.blog.recovery.controller;

import com.blog.recovery.domain.Session;
import com.blog.recovery.domain.Users;
import com.blog.recovery.repository.UserRepository;
import com.blog.recovery.request.SignUp;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.mock;

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


    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원가입에 성공해 200 코드를 응답한다.")
    void test6() throws Exception {
        //given
        SignUp request = SignUp.builder()
                .name("홍길동")
                .email("abc@naver.com")
                .password("abc123")
                .build();


        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signUp")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());


    }
}