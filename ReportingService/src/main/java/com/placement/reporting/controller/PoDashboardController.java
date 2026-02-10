package com.placement.reporting.controller;

import com.placement.reporting.dto.PoDashboardDto;
import com.placement.reporting.service.PoDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin
public class PoDashboardController {

    private final PoDashboardService poDashboardService;

    public PoDashboardController(PoDashboardService poDashboardService) {
        this.poDashboardService = poDashboardService;
    }

    @GetMapping("/po-dashboard")
    public ResponseEntity<PoDashboardDto> getPoDashboard(@RequestHeader("SESSION-TOKEN") String token) {
        return ResponseEntity.ok(poDashboardService.getDashboardData(token));
    }
}
