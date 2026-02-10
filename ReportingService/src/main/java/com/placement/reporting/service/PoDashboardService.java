package com.placement.reporting.service;

import com.placement.reporting.dto.PoDashboardDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("unchecked")
public class PoDashboardService {

    private final WebClient webClient;

    public PoDashboardService(WebClient webClient) {
        this.webClient = webClient;
    }

    public PoDashboardDto getDashboardData() {

        // 🔹 Total students (Team 2)
        List<Map<String, Object>> students = webClient.get()
                .uri("http://localhost:8082/api/students/list")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        long totalStudents = students.size();

        // 🔹 All jobs (Team 3)
        List<Map<String, Object>> jobs = webClient.get()
                .uri("http://localhost:8083/api/jobs/list")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        long totalJobs = jobs.size();

        long totalApplications = 0;
        long placedStudents = 0;

        double totalPackage = 0.0;
        long selectedCount = 0;

        // 🔹 For each job → fetch applications
        for (Map<String, Object> job : jobs) {

            Long jobId = Long.valueOf(job.get("id").toString());
            double pkg = Double.parseDouble(job.get("package").toString());

            List<Map<String, Object>> applications = webClient.get()
                    .uri("http://localhost:8084/api/applications/job/{id}", jobId)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();

            totalApplications += applications.size();

            for (Map<String, Object> app : applications) {
                if ("SELECTED".equalsIgnoreCase(
                        String.valueOf(app.get("status"))
                )) {
                    placedStudents++;
                    totalPackage += pkg;
                    selectedCount++;
                }
            }
        }

        double avgPackage = selectedCount > 0
                ? totalPackage / selectedCount
                : 0.0;

        return new PoDashboardDto(
                totalStudents,
                totalJobs,
                totalApplications,
                placedStudents,
                avgPackage
        );
    }
}
