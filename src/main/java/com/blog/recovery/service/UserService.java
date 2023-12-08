package com.blog.recovery.service;

import com.blog.recovery.domain.Session;
import com.blog.recovery.domain.Users;
import com.blog.recovery.exception.InvalidSignInfomation;
import com.blog.recovery.repository.UserRepository;
import com.blog.recovery.request.LoginDTO;
import com.blog.recovery.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long signIn(LoginDTO requestDTO){
        Users loginUser = userRepository
                .findByEmailAndPassword(requestDTO.getEmail(), requestDTO.getPassword())
                .orElseThrow(InvalidSignInfomation::new);

        Session session = loginUser.addSession();

        return loginUser.getId();
    }


}
