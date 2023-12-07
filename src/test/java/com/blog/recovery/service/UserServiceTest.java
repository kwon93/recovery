package com.blog.recovery.service;

import com.blog.recovery.domain.Users;
import com.blog.recovery.exception.InvalidSignInfomation;
import com.blog.recovery.repository.SessionRepository;
import com.blog.recovery.repository.UserRepository;
import com.blog.recovery.request.LoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        userRepository.deleteAllInBatch();

    }

    @Test
    @DisplayName("DTO로 넘어온 정보에 맞는 사용자 정보를 DB에서 찾아와야한다.")
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

        // when
        userService.signIn(loginRequest);
        //then
        Users user = userRepository.findById(usersInfo.getId()).get();

        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);

    }


    @Test
    @DisplayName("사용자 요청이 DB의 정보와 불일치경우 InvalidException 을 발생시켜야함.")
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

        LoginDTO invalidRequest = LoginDTO.builder()
                .email(email)
                .password(invalidPW)
                .build();
        // when then

        InvalidSignInfomation e = assertThrows(InvalidSignInfomation.class, () -> {
            userService.signIn(invalidRequest);
        });

        assertThat(e.getMessage()).isEqualTo("이메일과 비밀번호를 다시 확인해주세요.");

    }



}