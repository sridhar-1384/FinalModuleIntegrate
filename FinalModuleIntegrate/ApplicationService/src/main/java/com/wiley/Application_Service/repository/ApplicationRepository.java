package com.wiley.Application_Service.repository;

import com.wiley.Application_Service.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByStudentId(Long studentId);

    List<Application> findByJobId(Long jobId);

    boolean existsByStudentIdAndJobId(Long studentId, Long jobId);
}