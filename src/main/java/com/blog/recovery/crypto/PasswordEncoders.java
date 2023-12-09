package com.blog.recovery.crypto;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class PasswordEncoders {

    private static final SCryptPasswordEncoder encoder
            = new SCryptPasswordEncoder(16,
                                    8,
                                    1,
                                        32,
                                        64);

    public String encrypt(String rawPassword){
        return encoder.encode(rawPassword);
    }

    public Boolean matches(String rawPassword, String encodedPassword){
        return encoder.matches(rawPassword, encodedPassword);
    }

}
