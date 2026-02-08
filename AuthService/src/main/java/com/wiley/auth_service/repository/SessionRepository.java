package com.wiley.auth_service.repository;

import com.wiley.auth_service.model.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Sessions,Long> {
    Sessions findBySessionToken(String token);
}
