package com.wiley.student_service.service;

import com.wiley.student_service.dto.AuthUserDto;
import com.wiley.student_service.entity.MasterSkill;
import com.wiley.student_service.exception.DuplicateResourceException;
import com.wiley.student_service.repository.MasterSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SkillServiceImpl implements SkillService{
    private final MasterSkillRepository masterSkillRepository;
    private final AuthClient authClient;

    @Override
    public Iterable<MasterSkill> getAllSkills() {
        Iterable<MasterSkill> skills = masterSkillRepository.findAll();
        if (!skills.iterator().hasNext()) {
            throw new RuntimeException("No skills found");
        }
        return skills;
    }

    @Override
    public MasterSkill addSkill(String token,MasterSkill skill) {

        AuthUserDto user=authClient.validateSession(token);
        if(!user.getIsActive())
        {
            throw new RuntimeException("User session is inactive");
        }
        if (masterSkillRepository.existsByNameIgnoreCase(skill.getName())) {
            throw new DuplicateResourceException(
                    "Skill already exists with name: " + skill.getName()
            );
        }

        if(!user.getRole().equalsIgnoreCase("ADMIN"))
        {
            throw new RuntimeException("Only admins can add skills");
        }
        skill.setActive(true);
        return masterSkillRepository.save(skill);
    }

    @Override
    public List<MasterSkill> searchSkills(String query) {
        List<MasterSkill> skills =
                masterSkillRepository.findByNameContainingIgnoreCaseAndActiveTrue(query);

        if (skills.isEmpty()) {
            throw new RuntimeException(
                    "No active skills found matching: " + query
            );
        }
        return skills;
    }
}
