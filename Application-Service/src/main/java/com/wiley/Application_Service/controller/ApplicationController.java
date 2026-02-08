package com.wiley.Application_Service.controller;

import com.wiley.Application_Service.dto.ApplicationRequest;
import com.wiley.Application_Service.dto.ApplicationResponse;
import com.wiley.Application_Service.dto.StatusUpdateRequest;
import com.wiley.Application_Service.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final ApplicationService applicationService;

    // Apply to job -- student clicks on apply job
    @PostMapping("/apply")
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse applyToJob(
            @RequestHeader(value = "SESSION-TOKEN", required = false) String token,
            @RequestBody ApplicationRequest request) {
        try {
            return applicationService.applyToJob(token, request);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Get student's applications -- for student
    @GetMapping("/student/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ApplicationResponse> getStudentApplications(
            @RequestHeader(value = "SESSION-TOKEN", required = false) String token,
            @PathVariable Long id) {
        return applicationService.getStudentApplications(token, id);
    }

    // Get application details -- for student
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse getApplicationDetails(
            @RequestHeader(value = "SESSION-TOKEN", required = false) String token,
            @PathVariable Long id) {
        try {
            return applicationService.getApplicationDetails(token, id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    // Get applicants for a job -- for company_hr
    @GetMapping("/job/{jobId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ApplicationResponse> getJobApplicants(
            @RequestHeader(value = "SESSION-TOKEN", required = false) String token,
            @PathVariable Long jobId) {
        return applicationService.getJobApplicants(token, jobId);
    }

    // Update application status -- for company_hr
    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse updateStatus(
            @RequestHeader(value = "SESSION-TOKEN", required = false) String token,
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        try {
            return applicationService.updateStatus(token, id, request);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Get all applications -- for Placement Officer
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<ApplicationResponse> getAllApplications(
            @RequestHeader(value = "SESSION-TOKEN", required = false) String token) {
        return applicationService.getAllApplications(token);
    }
}