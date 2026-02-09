package com.placement.reporting.controller;

import com.placement.reporting.dto.PlacementReportDto;
import com.placement.reporting.service.AuthValidationService;
import com.placement.reporting.service.PlacementReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class PlacementReportController {

    private final PlacementReportService placementReportService;
    private final AuthValidationService authValidationService;

    public PlacementReportController(
            PlacementReportService placementReportService,
            AuthValidationService authValidationService) {
        this.placementReportService = placementReportService;
        this.authValidationService = authValidationService;
    }

    @GetMapping("/overview")
    public ResponseEntity<PlacementReportDto> getPlacementReport(
            @RequestHeader(value = "X-SESSION-TOKEN", required = false) String sessionToken) {

        authValidationService.validatePlacementOfficer(sessionToken);
        return ResponseEntity.ok(placementReportService.getPlacementReport());
    }


//    @GetMapping("/overview")
//    public ResponseEntity<PlacementReportDto> getPlacementReport(
//            @RequestHeader("X-SESSION-TOKEN") String sessionToken) {
//
//        // ADMIN check
//        authValidationService.validatePlacementOfficer(sessionToken);
//
//        // Business logic
//        PlacementReportDto report = placementReportService.getPlacementReport();
//        return ResponseEntity.ok(report);
//    }
}
