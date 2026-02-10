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
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody StudentRequestDTO dto) {
        return new ResponseEntity<>(studentService.createStudent(dto), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<StudentResponseDTO> getStudent(@RequestHeader ("Session-Token")String token) {
        return ResponseEntity.ok(studentService.getStudentById(token));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> getStudentByIdForHrandPo(@PathVariable("studentId") Long studentId, @RequestHeader ("Session-Token")String token) {
        return ResponseEntity.ok(studentService.getStudentByIdForHrandPo(studentId,token));
    }

    @GetMapping("/userId/{user_id}")
    public ResponseEntity<StudentResponseDTO> getStudentByUserId(@RequestHeader("Session-Token") String token,@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(studentService.getStudentByUserId(token, userId));
    }


    @PostMapping("add/skills")
    public ResponseEntity<String> addSkill(@RequestHeader("Session-Token") String token,
                                           @RequestBody AddSkillRequestDTO req) {
        studentService.addSkill(token, req.getMasterSkillId(), req.getLevel());
        return ResponseEntity.status(HttpStatus.CREATED).body("Skill added");
    }

    @DeleteMapping("/skills/{masterSkillId}")
    public ResponseEntity<String> removeSkill( @RequestHeader("Session-Token") String token,
                                               @PathVariable("masterSkillId") Long masterSkillId) {
        studentService.removeSkill(token, masterSkillId);
        return ResponseEntity.ok("Skill removed");
    }

    // Upload Resume
    @PostMapping("/resume")
    public ResponseEntity<String> uploadResume( @RequestHeader("Session-Token") String token,
                                                @RequestParam("file") MultipartFile file) {
        studentService.uploadResume(token, file);
        return ResponseEntity.ok("Resume uploaded successfully");
    }

    // Download Resume
    @GetMapping("/resume/download")
    public ResponseEntity<Resource> downloadResume( @RequestHeader("Session-Token") String token) {
        return studentService.downloadResume(token);
    }

    @GetMapping("/{studentId}/resume")
    public ResponseEntity<Resource> downloadingResume( @RequestHeader("Session-Token") String token,@PathVariable("studentId") Long studentId) {
        return studentService.downloadingResume(token,studentId);
    }
}
