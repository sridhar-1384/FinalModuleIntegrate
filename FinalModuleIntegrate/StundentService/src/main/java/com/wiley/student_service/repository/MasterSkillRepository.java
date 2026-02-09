package com.wiley.student_service.repository;

import com.wiley.student_service.entity.MasterSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MasterSkillRepository extends JpaRepository<MasterSkill,Long> {
    List<MasterSkill> findByNameContainingIgnoreCaseAndActiveTrue(String name);
    boolean existsByNameIgnoreCase(String name);

}
