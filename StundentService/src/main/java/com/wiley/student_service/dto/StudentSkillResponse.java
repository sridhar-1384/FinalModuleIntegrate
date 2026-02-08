package com.wiley.student_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSkillResponse {
    private Long skillId;
    private String skillName;
    private String category;
    private String level;
}
