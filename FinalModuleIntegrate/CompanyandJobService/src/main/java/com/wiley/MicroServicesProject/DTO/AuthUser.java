package com.wiley.MicroServicesProject.DTO;

import lombok.Data;

@Data
public class AuthUser {
    private Long userId;
    private String role;
    private boolean active;
    private String email;
}
