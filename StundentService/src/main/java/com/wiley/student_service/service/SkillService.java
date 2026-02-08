package com.wiley.student_service.service;

import com.wiley.student_service.entity.MasterSkill;

import java.util.List;

public interface SkillService {

    Iterable<MasterSkill> getAllSkills();

    MasterSkill addSkill(MasterSkill skill);

    List<MasterSkill> searchSkills(String query);
}
