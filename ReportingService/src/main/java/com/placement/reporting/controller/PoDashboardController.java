package com.placement.reporting.controller;

import com.placement.reporting.dto.PoDashboardDto;
import com.placement.reporting.service.AuthValidationService;
import com.placement.reporting.service.PoDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class PoDashboardController {

    private final PoDashboardService poDashboardService;
    private final AuthValidationService authValidationService;

    public PoDashboardController(
            PoDashboardService poDashboardService,
            AuthValidationService authValidationService) {
        this.poDashboardService = poDashboardService;
        this.authValidationService = authValidationService;
    }

    @GetMapping("/po-dashboard")
    public ResponseEntity<PoDashboardDto> getPoDashboard(
            @RequestHeader(value = "X-SESSION-TOKEN", required = false) String sessionToken) {

        authValidationService.validatePlacementOfficer(sessionToken);
        return ResponseEntity.ok(poDashboardService.getDashboardData());
    }
}
