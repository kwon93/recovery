package com.blog.recovery.domain;

import com.blog.recovery.request.SignUp;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime regDate;

    @Builder
    public Users(Long id,String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public static Users of(SignUp signUp, String encodedPassWord){
        return Users.builder()
                .name(signUp.getName())
                .email(signUp.getEmail())
                .password(encodedPassWord)
                .build();
    }
}
