package com.wiley.student_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_skills",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id", "master_skill_id"})
        })
public class StudentSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "master_skill_id", nullable = false)
    private MasterSkill masterSkill;

    private String level;        // BEGINNER / INTERMEDIATE / ADVANCED

}

