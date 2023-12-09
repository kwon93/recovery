package com.blog.recovery.service;

import com.blog.recovery.crypto.PasswordEncoders;
import com.blog.recovery.domain.Users;
import com.blog.recovery.exception.AlreadyExistEmailException;
import com.blog.recovery.repository.UserRepository;
import com.blog.recovery.request.SignUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;


    @Autowired
    PasswordEncoders encoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();

    }




    @Test
    @DisplayName("회원가입 성공.")
    void test4() throws Exception {
        //given

        SignUp request = SignUp.builder()
                .name("홍길동")
                .email("abc@naver.com")
                .password("abc123")
                .build();

        // when
        userService.signUp(request);

        //then
        List<Users> users = userRepository.findAll();

        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getName()).isEqualTo("홍길동");
        assertThat(users.get(0).getEmail()).isEqualTo("abc@naver.com");
        assertTrue(encoder.matches("abc123",users.get(0).getPassword()));

    }

    @Test
    @DisplayName("중복된 이메일 요청은 AlreadyExistException()이 발생해야한다.")
    void test5() throws Exception {
        //given
        final String email = "abc@naver.com";

        Users.builder()
                .name("권동혁")
                .email(email)
                .password("abc123")
                .build();

        userRepository.save(Users.builder()
                .name("권동혁")
                .email(email)
                .password("abc123")
                .build());


        SignUp request = SignUp.builder()
                .name("홍길동")
                .email("abc@naver.com")
                .password("abc123")
                .build();

        // when then
        AlreadyExistEmailException e = assertThrows(AlreadyExistEmailException.class,
                () -> {userService.signUp(request);});

        assertThat(e.getMessage()).isEqualTo("이미 가입된 이메일 입니다.");
    }



}