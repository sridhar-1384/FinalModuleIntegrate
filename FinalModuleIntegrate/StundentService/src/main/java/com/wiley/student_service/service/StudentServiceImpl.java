package com.wiley.student_service.service;

import com.wiley.student_service.dto.StudentRequestDTO;
import com.wiley.student_service.dto.StudentResponseDTO;
import com.wiley.student_service.dto.StudentSkillResponse;
import com.wiley.student_service.entity.MasterSkill;
import com.wiley.student_service.entity.Student;
import com.wiley.student_service.entity.StudentSkill;
import com.wiley.student_service.repository.MasterSkillRepository;
import com.wiley.student_service.repository.StudentRepository;
import com.wiley.student_service.repository.StudentSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final MasterSkillRepository masterSkillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {

        Student student = Student.builder()
                .userId(dto.getUserId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .dept(dto.getDept())
                .cgpa(dto.getCgpa())
                .build();

        return mapToResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponseDTO getStudentById(Long id) {
        return mapToResponse(studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found")));
    }

    @Override
    public StudentResponseDTO getStudentByUserId(String userId) {
        return mapToResponse(studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found")));
    }

    @Override
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    // ✅ PUT
    @Override
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setDept(dto.getDept());
        student.setCgpa(dto.getCgpa());

        return mapToResponse(studentRepository.save(student));
    }

    //  DELETE
    @Override
    public void deleteStudent(Long id) {
        if(!studentRepository.existsById(id))
            throw new RuntimeException("Student not found");
        studentRepository.deleteById(id);
    }

    // -------- SKILLS --------

    @Override
    public void addSkill(Long studentId, Long masterSkillId, String level) {

        Student student = studentRepository.findById(studentId).orElseThrow();
        MasterSkill masterSkill = masterSkillRepository.findById(masterSkillId).orElseThrow();

        if (studentSkillRepository.existsByStudentAndMasterSkill(student, masterSkill))
            throw new RuntimeException("Skill already added");

        StudentSkill ss = new StudentSkill();
        ss.setStudent(student);
        ss.setMasterSkill(masterSkill);
        ss.setLevel(level);

        studentSkillRepository.save(ss);
    }

    @Override
    public void removeSkill(Long studentId, Long masterSkillId) {

        Student student = studentRepository.findById(studentId).orElseThrow();

        StudentSkill ss = studentSkillRepository.findByStudent(student)
                .stream()
                .filter(s -> s.getMasterSkill().getId().equals(masterSkillId))
                .findFirst()
                .orElseThrow();

        studentSkillRepository.delete(ss);
    }

    // -------- RESUME --------

    @Override
    public void uploadResume(Long studentId, MultipartFile file) {

        Student student = studentRepository.findById(studentId).orElseThrow();

        try {
            String folder = System.getProperty("user.dir") + "/uploads/resumes/";
            new File(folder).mkdirs();

            String path = folder + student.getEmail() + "_resume.pdf";

            file.transferTo(new File(path));

            student.setResumePath(path);
            studentRepository.save(student);

        } catch (Exception e) {
            throw new RuntimeException("Upload failed");
        }
    }

    @Override
    public ResponseEntity<Resource> downloadResume(Long studentId) {

        Student student = studentRepository.findById(studentId).orElseThrow();

        try {
            File file = new File(student.getResumePath());
            Resource resource = new UrlResource(file.toURI());

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + file.getName())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Download failed");
        }
    }

    // -------- MAPPER --------

    private StudentResponseDTO mapToResponse(Student student) {

        List<StudentSkillResponse> skills =
                studentSkillRepository.findByStudent(student)
                        .stream()
                        .map(ss -> new StudentSkillResponse(
                                ss.getMasterSkill().getId(),
                                ss.getMasterSkill().getName(),
                                ss.getMasterSkill().getCategory(),
                                ss.getLevel()))
                        .toList();

        return StudentResponseDTO.builder()
                .id(student.getId())
                .userId(student.getUserId())
                .name(student.getName())
                .email(student.getEmail())
                .dept(student.getDept())
                .cgpa(student.getCgpa())
                .resumePath(student.getResumePath())
                .skills(skills)
                .build();
    }
}
