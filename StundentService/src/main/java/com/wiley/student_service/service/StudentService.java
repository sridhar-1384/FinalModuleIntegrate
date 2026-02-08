package com.wiley.student_service.service;

import com.wiley.student_service.dto.StudentRequestDTO;
import com.wiley.student_service.dto.StudentResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {

    StudentResponseDTO createStudent(StudentRequestDTO dto);

    StudentResponseDTO getStudentById(Long id);

    StudentResponseDTO getStudentByUserId(String userId);

    List<StudentResponseDTO> getAllStudents();

    StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto);

    void deleteStudent(Long id);

    void addSkill(Long studentId, Long masterSkillId, String level);

    void uploadResume(Long studentId, MultipartFile file);

    ResponseEntity<Resource> downloadResume(Long studentId);

    void removeSkill(Long studentId, Long masterSkillId);
}
