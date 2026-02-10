package com.wiley.student_service.service;

import com.wiley.student_service.dto.AuthUserDto;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {


    private final AuthClient authClient;
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
                .phone(dto.getPhone())
                .build();

        return mapToResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponseDTO getStudentById(String token) {
        AuthUserDto user = authClient.validateSession(token);

                Student student = studentRepository.findByUserId(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

        if(!user.getIsActive()) {
            throw new RuntimeException("Inactive user");
        }
        if (!student.getUserId().equals(user.getUserId())
                && !user.getRole().equals("PLACEMENT_OFFICER")) {
            throw new RuntimeException("Unauthorized access");
        }
        return mapToResponse(student);
    }

    @Override
    public StudentResponseDTO getStudentByIdForHrandPo(Long studentId,String token) {
        AuthUserDto user = authClient.validateSession(token);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if(!user.getIsActive()) {
            throw new RuntimeException("Inactive user");
        }
        if (!user.getRole().equals("COMPANY_HR") && !user.getRole().equals("PLACEMENT_OFFICER") && !user.getRole().equals("STUDENT")) {
            throw new RuntimeException("Unauthorized access");
        }
        return mapToResponse(student);
    }

    @Override
    public StudentResponseDTO getStudentByUserId(String token, Long userId) {

        AuthUserDto user = authClient.validateSession(token);

        if (!user.getIsActive())
            throw new RuntimeException("Inactive user");

        if (!user.getUserId().equals(userId) && !user.getRole().equals("ADMIN"))
            throw new RuntimeException("Unauthorized access");

        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return mapToResponse(student);
    }

    @Override
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    // ✅ PUT
    @Override
    public StudentResponseDTO updateStudent(String token, Long id, StudentRequestDTO dto) {

        AuthUserDto user = authClient.validateSession(token);

        if (!user.getIsActive()) {
            throw new RuntimeException("Inactive user");
        }
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!student.getUserId().equals(user.getUserId())
                && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("Unauthorized access");
        }

        student.setDept(dto.getDept());
        student.setCgpa(dto.getCgpa());

        return mapToResponse(studentRepository.save(student));
    }


    //  DELETE
//    @Override
//    public void deleteStudent(String token,Long id) {
//        if(!studentRepository.existsById(id))
//            throw new RuntimeException("Student not found");
//
//        User user= Session.
//        studentRepository.deleteById(id);
//    }
    @Override
    public void deleteStudent(String token, Long studentId) {

        AuthUserDto user = authClient.validateSession(token);

        if (!user.getIsActive()) {
            throw new RuntimeException("Inactive user");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Ownership / role check
        if (!student.getUserId().equals(user.getUserId())
                && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("Unauthorized access");
        }

        studentRepository.delete(student);
    }

    // -------- SKILLS --------

    @Override
    public void addSkill(String token, Long masterSkillId, String level) {

        AuthUserDto user = authClient.validateSession(token);
        Student student = studentRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        authorize(user, student);

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
    public void removeSkill(String token,Long masterSkillId) {

        AuthUserDto user = authClient.validateSession(token);
        Student student = studentRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        authorize(user, student);

        StudentSkill ss = studentSkillRepository.findByStudent(student)
                .stream()
                .filter(s -> s.getMasterSkill().getId().equals(masterSkillId))
                .findFirst()
                .orElseThrow();

        studentSkillRepository.delete(ss);
    }

    // -------- RESUME --------

    public void uploadResume(String token, MultipartFile file) {

        AuthUserDto user = authClient.validateSession(token);

        Student student = studentRepository.findByUserId(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

        authorize(user, student);

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
    public ResponseEntity<Resource> downloadResume(String token) {

        AuthUserDto user = authClient.validateSession(token);
        Student student = studentRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        authorize(user, student);
        if (!user.getRole().equals("COMPANY_HR") && !user.getRole().equals("PLACEMENT_OFFICER")) {
            throw new RuntimeException("Unauthorized access");
        }

        try {
            File file = new File(student.getResumePath());
            Resource resource = new UrlResource(file.toURI());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition", "attachment; filename=" + file.getName())
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Download failed");
        }
    }

    private void authorize(AuthUserDto user, Student student) {
        if (!student.getUserId().equals(user.getUserId())
                && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("Unauthorized access");
        }
    }

//    @Override
//    public ResponseEntity<Resource> downloadingResume(String token,Long studentId) {
//
//        AuthUserDto user = authClient.validateSession(token);
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        if (!user.getRole().equals("COMPANY_HR") && !user.getRole().equals("PLACEMENT_OFFICER")) {
//            throw new RuntimeException("Unauthorized access");
//        }
//
//        try {
//            File file = new File(student.getResumePath());
//            Resource resource = new UrlResource(file.toURI());
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.APPLICATION_PDF)
//                    .header("Content-Disposition", "attachment; filename=" + file.getName())
//                    .body(resource);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Download failed");
//        }
//    }


    @Override
    public ResponseEntity<Resource> downloadingResume(String token, Long studentId) {

        AuthUserDto user = authClient.validateSession(token);

        if (!user.getRole().equals("COMPANY_HR")
                && !user.getRole().equals("PLACEMENT_OFFICER")) {
            throw new RuntimeException("Unauthorized access");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (student.getResumePath() == null) {
            throw new RuntimeException("Resume not uploaded");
        }

        File file = new File(student.getResumePath());

        if (!file.exists()) {
            throw new RuntimeException("Resume file not found");
        }

        try {
            Resource resource = new UrlResource(file.toURI());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + file.getName() + "\""
                    )
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid resume file path", e);
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
                .phone(student.getPhone())
                .resumePath(student.getResumePath())
                .skills(skills)
                .build();
    }
}
