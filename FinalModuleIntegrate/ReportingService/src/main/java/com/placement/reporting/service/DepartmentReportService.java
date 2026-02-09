package com.placement.reporting.service;

import com.placement.reporting.dto.DepartmentReportDto;
import com.placement.reporting.repository.DepartmentReportRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentReportService {

    private final DepartmentReportRepository repository;

    public DepartmentReportService(DepartmentReportRepository repository) {
        this.repository = repository;
    }

    public List<DepartmentReportDto> getDepartmentReport() {

        List<Object[]> rows = repository.fetchDepartmentReport();
        List<DepartmentReportDto> result = new ArrayList<>();

        for (Object[] row : rows) {
            String department = (String) row[0];
            Long totalStudents = row[1] != null ? ((Number) row[1]).longValue() : 0L;
            Long placedStudents = row[2] != null ? ((Number) row[2]).longValue() : 0L;
            Double averagePackage = row[3] != null ? ((Number) row[3]).doubleValue() : null;

            result.add(new DepartmentReportDto(
                    department,
                    totalStudents,
                    placedStudents,
                    averagePackage
            ));
        }

        return result;
    }
}
