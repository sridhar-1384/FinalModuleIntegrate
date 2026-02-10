package com.placement.reporting.service;

import com.placement.reporting.dto.PlacementReportDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("unchecked")
public class PlacementReportService {

    private final WebClient webClient;

    public PlacementReportService(WebClient webClient) {
        this.webClient = webClient;
    }

    public PlacementReportDto getPlacementReport() {

        // 🔹 Get all students (Team 2)
        List<Map<String, Object>> students = webClient.get()
                .uri("http://localhost:8082/api/students/list")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        // 🔹 Get all jobs (Team 3)
        List<Map<String, Object>> jobs = webClient.get()
                .uri("http://localhost:8083/api/jobs/list")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        long totalStudents = students.size();

        long placedStudents = 0;
        double totalPackage = 0.0;
        double highestPackage = 0.0;

        // 🔹 For each job → get applications
        for (Map<String, Object> job : jobs) {

            Long jobId = Long.valueOf(job.get("id").toString());
            double pkg = Double.parseDouble(job.get("package").toString());

            List<Map<String, Object>> applications = webClient.get()
                    .uri("http://localhost:8084/api/applications/job/{id}", jobId)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();

            for (Map<String, Object> app : applications) {
                if ("SELECTED".equalsIgnoreCase(
                        String.valueOf(app.get("status"))
                )) {
                    placedStudents++;
                    totalPackage += pkg;
                    highestPackage = Math.max(highestPackage, pkg);
                }
            }
        }

        double avgPackage = placedStudents > 0
                ? totalPackage / placedStudents
                : 0.0;

        double placedPercentage = totalStudents > 0
                ? (placedStudents * 100.0) / totalStudents
                : 0.0;

        // 🔹 Get companies visited (Team 3)
        List<Map<String, Object>> companies = webClient.get()
                .uri("http://localhost:8083/api/companies/list")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        long companiesVisited = companies.size();

        return new PlacementReportDto(
                totalStudents,
                placedStudents,
                placedPercentage,
                avgPackage,
                highestPackage,
                companiesVisited
        );
    }
}
