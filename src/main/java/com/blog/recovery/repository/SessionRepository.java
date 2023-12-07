package com.blog.recovery.repository;

import com.blog.recovery.domain.Session;
import com.blog.recovery.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByAccessToken(String token);
}

