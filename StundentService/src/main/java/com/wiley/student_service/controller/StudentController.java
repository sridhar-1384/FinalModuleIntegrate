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
    public ResponseEntity<StudentResponseDTO> getStudent(@RequestHeader ("Session-Token")String token,@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(token,id));
    }

    @GetMapping("/userId/{user_id}")
    public ResponseEntity<StudentResponseDTO> getStudentByUserId(@RequestHeader("Session-Token") String token,@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(studentService.getStudentByUserId(token, userId));
    }


    @PostMapping("/{studentId}/skills")
    public ResponseEntity<String> addSkill(@RequestHeader("Session-Token") String token,
                                           @PathVariable Long studentId,
                                           @RequestBody AddSkillRequestDTO req) {
        studentService.addSkill(token,studentId, req.getMasterSkillId(), req.getLevel());
        return ResponseEntity.status(HttpStatus.CREATED).body("Skill added");
    }

    @DeleteMapping("/{studentId}/skills/{masterSkillId}")
    public ResponseEntity<String> removeSkill( @RequestHeader("Session-Token") String token,
                                               @PathVariable Long studentId,
                                               @PathVariable Long masterSkillId) {
        studentService.removeSkill(token,studentId, masterSkillId);
        return ResponseEntity.ok("Skill removed");
    }

    // Upload Resume
    @PostMapping("/{id}/resume")
    public ResponseEntity<String> uploadResume( @RequestHeader("Session-Token") String token,@PathVariable Long id,
                                                @RequestParam("file") MultipartFile file) {
        studentService.uploadResume(token,id, file);
        return ResponseEntity.ok("Resume uploaded successfully");
    }

    // Download Resume
    @GetMapping("/{id}/resume/download")
    public ResponseEntity<Resource> downloadResume( @RequestHeader("Session-Token") String token,@PathVariable Long id) {
        return studentService.downloadResume(token,id);
    }
}
