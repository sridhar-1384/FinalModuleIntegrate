package com.wiley.student_service.controller;

import com.wiley.student_service.dto.AddSkillRequestDTO;
import com.wiley.student_service.dto.StudentRequestDTO;
import com.wiley.student_service.dto.StudentResponseDTO;
import com.wiley.student_service.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody StudentRequestDTO dto) {
        return new ResponseEntity<>(studentService.createStudent(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/userId/{user_id}")
    public ResponseEntity<StudentResponseDTO> getStudentByUserId(@PathVariable("user_id") String userId) {
        return ResponseEntity.ok(studentService.getStudentByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }


    @PostMapping("/{studentId}/skills")
    public ResponseEntity<String> addSkill(@PathVariable Long studentId,
                                           @RequestBody AddSkillRequestDTO req) {
        studentService.addSkill(studentId, req.getMasterSkillId(), req.getLevel());
        return ResponseEntity.status(HttpStatus.CREATED).body("Skill added");
    }

    @DeleteMapping("/{studentId}/skills/{masterSkillId}")
    public ResponseEntity<String> removeSkill(@PathVariable Long studentId,
                                              @PathVariable Long masterSkillId) {
        studentService.removeSkill(studentId, masterSkillId);
        return ResponseEntity.ok("Skill removed");
    }

    // Upload Resume
    @PostMapping("/{id}/resume")
    public ResponseEntity<String> uploadResume(@PathVariable Long id,
                                               @RequestParam("file") MultipartFile file) {
        studentService.uploadResume(id, file);
        return ResponseEntity.ok("Resume uploaded successfully");
    }

    // Download Resume
    @GetMapping("/{id}/resume/download")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long id) {
        return studentService.downloadResume(id);
    }
}
