package com.wiley.student_service.repository;

import com.wiley.student_service.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

//import java.lang.ScopedValue;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserId(String userId);
}
