package com.wiley.student_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSkillRequestDTO {
    private Long masterSkillId;
    private String level;
}
