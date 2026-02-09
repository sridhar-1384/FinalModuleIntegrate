package com.wiley.student_service.dto;

import lombok.Data;

@Data
public class AuthUserDto {
    private Long userId;
    private String email;
    private String role;
    private Boolean isActive;
}
