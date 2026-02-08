package com.wiley.auth_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sessions")
@AllArgsConstructor
@NoArgsConstructor
public class Sessions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private String sessionToken;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
