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

    public CompanyStudentReportController(CompanyStudentReportService companyStudentReportService) {
        this.companyStudentReportService = companyStudentReportService;
    }

    @GetMapping("/company-wise")
    public List<CompanyReportDto> getCompanyWiseReport() {
        return companyStudentReportService.getCompanyWiseReport();
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
    public ResponseEntity<byte[]> exportReportPdf() {
        return companyStudentReportService.exportPdfReport();
    }
}























































//package com.placement.reporting.controller;
//
//import com.placement.reporting.auth.AuthValidationService;
//import com.placement.reporting.dto.CompanyReportDto;
//import com.placement.reporting.dto.StudentOverallStatsDto;
//import com.placement.reporting.dto.StudentStatsDto;
//import com.placement.reporting.service.CompanyStudentReportService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/reports")
//public class CompanyStudentReportController {
//
//    private final CompanyStudentReportService companyStudentReportService;
//    private final AuthValidationService authValidationService;
//
//    public CompanyStudentReportController(CompanyStudentReportService companyStudentReportService,
//                                          AuthValidationService authValidationService) {
//        this.companyStudentReportService = companyStudentReportService;
//        this.authValidationService = authValidationService;
//    }
//
//    @GetMapping("/company-wise")
//    public List<CompanyReportDto> getCompanyWiseReport(
//            @RequestHeader("X-SESSION-TOKEN") String sessionToken) {
//
//        authValidationService.validatePlacementOfficer(sessionToken);
//        return companyStudentReportService.getCompanyWiseReport();
//    }
//
//    @GetMapping("/student")
//    public StudentOverallStatsDto getStudentOverallStats(
//            @RequestHeader("X-SESSION-TOKEN") String sessionToken) {
//
//        authValidationService.validatePlacementOfficer(sessionToken);
//        return companyStudentReportService.getStudentOverallStats();
//    }
//
//    @GetMapping("/student/{id}")
//    public StudentStatsDto getStudentStats(
//            @RequestHeader("X-SESSION-TOKEN") String sessionToken,
//            @PathVariable Long id) {
//
//        authValidationService.validatePlacementOfficer(sessionToken);
//        return companyStudentReportService.getStudentStats(id);
//    }
//
//    @GetMapping("/export")
//    public ResponseEntity<byte[]> exportReportPdf(
//            @RequestHeader("X-SESSION-TOKEN") String sessionToken) {
//
//        authValidationService.validatePlacementOfficer(sessionToken);
//        return companyStudentReportService.exportPdfReport();
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////package com.placement.reporting.controller;
////
////import com.placement.reporting.dto.CompanyReportDto;
////import com.placement.reporting.dto.StudentOverallStatsDto;
////import com.placement.reporting.dto.StudentStatsDto;
////import com.placement.reporting.service.ReportService;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.List;
////
////
////@RestController
////@RequestMapping("/api/reports")
////public class ReportController {
////
////    private final ReportService reportService;
////
////    public ReportController(ReportService reportService) {
////        this.reportService = reportService;
////    }
////
////    @GetMapping("/company-wise")
////    public List<CompanyReportDto> getCompanyWiseReport() {
////        return reportService.getCompanyWiseReport();
////    }
////
////    @GetMapping("/student")
////    public StudentOverallStatsDto getStudentOverallStats() {
////        return reportService.getStudentOverallStats();
////    }
////
////    @GetMapping("/export")
////    public ResponseEntity<byte[]> exportReportPdf() {
////        return reportService.exportPdfReport();
////    }
////
////
////    @GetMapping("/student/{id}")
////    public StudentStatsDto getStudentStats(@PathVariable Long id) {
////        return reportService.getStudentStats(id);
////    }
////
////}
