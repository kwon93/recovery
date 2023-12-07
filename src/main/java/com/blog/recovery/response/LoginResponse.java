package com.blog.recovery.response;

import com.blog.recovery.domain.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {

    private String email;

    private String password;

    @Builder
    public LoginResponse(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static LoginResponse of(Users users){
        return LoginResponse.builder()
                .email(users.getEmail())
                .password(users.getPassword())
                .build();
    }


}
