package com.example.userauthenticationservice.repos;

import com.example.userauthenticationservice.models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByTokenAndId(String token, Long id);

}
