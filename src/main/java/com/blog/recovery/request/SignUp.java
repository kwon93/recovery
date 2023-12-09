package com.blog.recovery.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SignUp {

    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    @Builder
    public SignUp(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
