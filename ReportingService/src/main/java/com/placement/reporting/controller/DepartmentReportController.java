package com.placement.reporting.controller;

import com.placement.reporting.dto.DepartmentReportDto;
import com.placement.reporting.service.DepartmentReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class DepartmentReportController {

    private final DepartmentReportService departmentReportService;

    public DepartmentReportController(
            DepartmentReportService departmentReportService) {
        this.departmentReportService = departmentReportService;
    }

    // Department-wise placement report
    @GetMapping("/department-wise")
    public ResponseEntity<List<DepartmentReportDto>> getDepartmentWiseReport() {
        List<DepartmentReportDto> report =
                departmentReportService.getDepartmentReport();
        return ResponseEntity.ok(report);
    }
}
