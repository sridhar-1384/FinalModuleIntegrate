package com.wiley.student_service.controller;

import com.wiley.student_service.dto.StudentRequestDTO;
import com.wiley.student_service.dto.StudentResponseDTO;
import com.wiley.student_service.entity.MasterSkill;
import com.wiley.student_service.service.SkillService;
import com.wiley.student_service.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final StudentService studentService;
    private final SkillService service;

    @DeleteMapping("/delete-student/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("Session-Token") String token,@PathVariable Long id) {
        studentService.deleteStudent(token,id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add-skill")
    public MasterSkill addSkill(@RequestHeader("Session-Token") String token,@RequestBody MasterSkill skill) {
        return service.addSkill(token,skill);
    }

    @PutMapping("/update-student/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(@RequestHeader("Session-Token") String token,@PathVariable Long id,
                                                            @RequestBody StudentRequestDTO dto) {
        return ResponseEntity.ok(studentService.updateStudent(token,id, dto));
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }
}
