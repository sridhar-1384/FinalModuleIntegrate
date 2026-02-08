package com.wiley.student_service.repository;

import com.wiley.student_service.entity.MasterSkill;
import com.wiley.student_service.entity.Student;
import com.wiley.student_service.entity.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentSkillRepository extends JpaRepository<StudentSkill,Long> {
    boolean existsByStudentAndMasterSkill(Student student, MasterSkill masterSkill);

    List<StudentSkill> findByStudent(Student student);
}
