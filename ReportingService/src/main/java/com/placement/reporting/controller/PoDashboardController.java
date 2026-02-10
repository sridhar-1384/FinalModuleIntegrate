package com.placement.reporting.controller;

import com.placement.reporting.dto.PoDashboardDto;
import com.placement.reporting.service.PoDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class PoDashboardController {

    private final PoDashboardService poDashboardService;

    public PoDashboardController(
            PoDashboardService poDashboardService) {
        this.poDashboardService = poDashboardService;
    }

    // Placement Officer dashboard summary
    @GetMapping("/po-dashboard")
    public ResponseEntity<PoDashboardDto> getPoDashboard() {
        PoDashboardDto dashboard =
                poDashboardService.getDashboardData();
        return ResponseEntity.ok(dashboard);
    }
}
