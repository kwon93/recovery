package com.blog.recovery.service;

import com.blog.recovery.domain.Users;
import com.blog.recovery.exception.AlreadyExistEmailException;
import com.blog.recovery.repository.UserRepository;
import com.blog.recovery.request.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void signUp(SignUp requestDTO) {

        Optional<Users> usersOptional = userRepository.findByEmail(requestDTO.getEmail());

        if (usersOptional.isPresent()){
            throw new AlreadyExistEmailException();
        }

        String encryptedPassoword
                = passwordEncoder.encode(requestDTO.getPassword());


        Users users = Users.of(requestDTO, encryptedPassoword);

        userRepository.save(users);
    }
}
