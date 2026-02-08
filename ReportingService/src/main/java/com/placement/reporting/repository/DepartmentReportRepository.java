package com.placement.reporting.repository;

import com.placement.reporting.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentReportRepository extends JpaRepository<Application, Long> {

    @Query(value = """
        SELECT 
            s.department            AS department,
            COUNT(DISTINCT s.id)    AS totalStudents,
            SUM(CASE 
                WHEN a.status = 'SELECTED' THEN 1 
                ELSE 0 
            END)                    AS placedStudents,
            AVG(CASE 
                WHEN a.status = 'SELECTED' THEN j.`package` 
                ELSE NULL 
            END)                    AS averagePackage
        FROM students s
        LEFT JOIN applications a ON s.id = a.student_id
        LEFT JOIN jobs j ON a.job_id = j.id
        GROUP BY s.department
        ORDER BY s.department
    """, nativeQuery = true)
    List<Object[]> fetchDepartmentReport();
}
