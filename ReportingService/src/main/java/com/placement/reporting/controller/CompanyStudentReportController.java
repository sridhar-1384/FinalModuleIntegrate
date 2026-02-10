package com.placement.reporting.controller;

import com.placement.reporting.auth.AuthValidationService;
import com.placement.reporting.dto.CompanyReportDto;
import com.placement.reporting.dto.StudentOverallStatsDto;
import com.placement.reporting.dto.StudentStatsDto;
import com.placement.reporting.service.CompanyStudentReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin
public class CompanyStudentReportController {

    private final CompanyStudentReportService companyStudentReportService;

    public CompanyStudentReportController(CompanyStudentReportService companyStudentReportService) {
        this.companyStudentReportService = companyStudentReportService;
    }

    @GetMapping("/company-wise")
    public List<CompanyReportDto> getCompanyWiseReport(@RequestHeader("SESSION-TOKEN") String token) {
        return companyStudentReportService.getCompanyWiseReport(token);
    }

    @GetMapping("/student")
    public StudentOverallStatsDto getStudentOverallStats() {
        return companyStudentReportService.getStudentOverallStats();
    }

    @GetMapping("/student/{id}")
    public StudentStatsDto getStudentStats(@PathVariable Long id) {
        return companyStudentReportService.getStudentStats(id);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportReportPdf(@RequestHeader("SESSION-TOKEN") String token) {
        return companyStudentReportService.exportPdfReport(token);
    }
}
