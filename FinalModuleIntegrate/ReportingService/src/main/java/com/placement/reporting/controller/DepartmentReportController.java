package com.placement.reporting.controller;

import com.placement.reporting.dto.DepartmentReportDto;
import com.placement.reporting.service.AuthValidationService;
import com.placement.reporting.service.DepartmentReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class DepartmentReportController {

    private final DepartmentReportService departmentReportService;
    private final AuthValidationService authValidationService;

    public DepartmentReportController(
            DepartmentReportService departmentReportService,
            AuthValidationService authValidationService) {
        this.departmentReportService = departmentReportService;
        this.authValidationService = authValidationService;
    }

    @GetMapping("/department-wise")
    public ResponseEntity<List<DepartmentReportDto>> getDepartmentWiseReport(
            @RequestHeader(value = "X-SESSION-TOKEN", required = false) String sessionToken) {

        authValidationService.validatePlacementOfficer(sessionToken);
        return ResponseEntity.ok(departmentReportService.getDepartmentReport());
    }


//    @GetMapping("/department-wise")
//    public ResponseEntity<List<DepartmentReportDto>> getDepartmentWiseReport(
//            @RequestHeader("X-SESSION-TOKEN") String sessionToken) {
//
//        // ADMIN check
//        authValidationService.validatePlacementOfficer(sessionToken);
//
//        // Business logic
//        List<DepartmentReportDto> report =
//                departmentReportService.getDepartmentReport();
//
//        return ResponseEntity.ok(report);
//    }
}
