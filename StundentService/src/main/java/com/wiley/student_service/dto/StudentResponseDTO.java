package com.wiley.student_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StudentResponseDTO {

    private Long id;
    private String userId;
    private String name;
    private String email;
    private String dept;
    private Double cgpa;
    private String phone;
    private String resumePath;
    private List<StudentSkillResponse> skills;
}
