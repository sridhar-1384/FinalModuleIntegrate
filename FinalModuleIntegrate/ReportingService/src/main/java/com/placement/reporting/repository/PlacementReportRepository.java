package com.placement.reporting.repository;

import com.placement.reporting.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface PlacementReportRepository extends JpaRepository<Application, Long> {

    // Total students
    @Query(value = "SELECT COUNT(*) FROM students", nativeQuery = true)
    Long countTotalStudents();

    // Placed students
    @Query(value = """
        SELECT COUNT(*)
        FROM applications
        WHERE status = 'SELECTED'
    """, nativeQuery = true)
    Long countPlacedStudents();

    // Average package (only selected)
    @Query(value = """
        SELECT AVG(j.`package`)
        FROM applications a
        JOIN jobs j ON a.job_id = j.id
        WHERE a.status = 'SELECTED'
    """, nativeQuery = true)
    Double findAveragePackage();

    // Highest package (only selected)
    @Query(value = """
        SELECT MAX(j.`package`)
        FROM applications a
        JOIN jobs j ON a.job_id = j.id
        WHERE a.status = 'SELECTED'
    """, nativeQuery = true)
    Double findHighestPackage();

    // Companies visited
    @Query(value = """
        SELECT COUNT(DISTINCT j.company_id)
        FROM jobs j
    """, nativeQuery = true)
    Long countCompaniesVisited();
}
