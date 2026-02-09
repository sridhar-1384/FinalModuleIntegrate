package com.wiley.student_service.service;

import com.wiley.student_service.dto.StudentRequestDTO;
import com.wiley.student_service.dto.StudentResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {

    StudentResponseDTO createStudent(StudentRequestDTO dto);

    StudentResponseDTO getStudentById(String token);

    StudentResponseDTO getStudentByUserId(String token,Long userId);

//    StudentResponseDTO getStudentByUserId(String token, String userId);

    List<StudentResponseDTO> getAllStudents();

    StudentResponseDTO updateStudent(String token,Long id, StudentRequestDTO dto);

    void deleteStudent(String token,Long id);

    void addSkill(String token, Long masterSkillId, String level);

    void uploadResume(String token, MultipartFile file);

    ResponseEntity<Resource> downloadResume(String token);

    void removeSkill(String token,Long studentId, Long masterSkillId);
}
