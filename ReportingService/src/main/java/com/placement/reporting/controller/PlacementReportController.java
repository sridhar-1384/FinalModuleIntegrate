package com.placement.reporting.controller;

import com.placement.reporting.dto.PlacementReportDto;
import com.placement.reporting.service.PlacementReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class PlacementReportController {

    private final PlacementReportService placementReportService;

    public PlacementReportController(PlacementReportService placementReportService) {
        this.placementReportService = placementReportService;
    }

    @GetMapping("/overview")
    public ResponseEntity<PlacementReportDto> getPlacementReport() {
        return ResponseEntity.ok(placementReportService.getPlacementReport());
    }
}