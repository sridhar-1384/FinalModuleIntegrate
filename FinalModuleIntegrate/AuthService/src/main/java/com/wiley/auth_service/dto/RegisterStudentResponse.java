package com.wiley.auth_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterStudentResponse {
    private String status;
    private String message;
}
