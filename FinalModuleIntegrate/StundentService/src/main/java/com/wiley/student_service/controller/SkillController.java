package com.wiley.student_service.controller;

import com.wiley.student_service.entity.MasterSkill;
import com.wiley.student_service.repository.MasterSkillRepository;
import com.wiley.student_service.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService service;

    @GetMapping
    public Iterable<MasterSkill> getAllSkills() {
        return service.getAllSkills();
    }

    @GetMapping("/search")
    public List<MasterSkill> searchSkills(@RequestParam String query) {
        return service.searchSkills(query);
    }

}
