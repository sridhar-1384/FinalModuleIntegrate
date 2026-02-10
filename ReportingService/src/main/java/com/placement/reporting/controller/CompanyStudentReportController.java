package com.placement.reporting.controller;

import com.placement.reporting.dto.CompanyReportDto;
import com.placement.reporting.dto.StudentOverallStatsDto;
import com.placement.reporting.dto.StudentStatsDto;
import com.placement.reporting.service.CompanyStudentReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class CompanyStudentReportController {

    private final CompanyStudentReportService companyStudentReportService;

    public CompanyStudentReportController(
            CompanyStudentReportService companyStudentReportService) {
        this.companyStudentReportService = companyStudentReportService;
    }

    // Company-wise placement report
    @GetMapping("/company-wise")
    public List<CompanyReportDto> getCompanyWiseReport() {
        return companyStudentReportService.getCompanyWiseReport();
    }

    // Overall student statistics (PO view)
    @GetMapping("/student")
    public StudentOverallStatsDto getStudentOverallStats() {
        return companyStudentReportService.getStudentOverallStats();
    }

    // Individual student statistics
    @GetMapping("/student/{id}")
    public StudentStatsDto getStudentStats(@PathVariable("id") Long studentId) {
        return companyStudentReportService.getStudentStats(studentId);
    }

    // Export company-wise report as PDF
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportReportPdf() {
        return companyStudentReportService.exportPdfReport();
    }
}
