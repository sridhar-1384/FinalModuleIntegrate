package com.wiley.auth_service.dto;


import lombok.Data;

import java.util.List;

@Data
public class StudentRequestDTO {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String dept;
    private Double cgpa;
    private String phone;
    private List<String> skills;
}
