package com.blog.recovery.repository;

import com.blog.recovery.domain.Users;
import com.blog.recovery.request.LoginDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("이메일과 비밀번호와 일치하는 Users를 DB에서 가져온다.")
    void test() throws Exception {
        //given
        final String email = "kwon@naver.com";
        final String password = "k1234";

        Users usersInfo = Users.builder()
                .id(2L)
                .email(email)
                .password(password)
                .build();

        userRepository.save(usersInfo);

        LoginDTO loginRequest = LoginDTO.builder()
                .email(email)
                .password(password)
                .build();

        // when
        Users users = userRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword()).get();
        //then
        assertThat(users).isNotNull();
        assertThat(users.getEmail()).isEqualTo(email);

    }
}