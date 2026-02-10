package com.placement.reporting.service;

import com.placement.reporting.dto.DepartmentReportDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class DepartmentReportService {

    private final WebClient webClient;

    public DepartmentReportService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<DepartmentReportDto> getDepartmentReport() {

        // 🔹 Fetch all students (Team 2)
        List<Map<String, Object>> students = webClient.get()
                .uri("http://localhost:8082/api/students/list")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        Map<String, Long> totalByDept = new HashMap<>();
        Map<String, Long> placedByDept = new HashMap<>();

        for (Map<String, Object> student : students) {

            String department = String.valueOf(student.get("department"));
            Long studentId = Long.valueOf(student.get("id").toString());

            totalByDept.put(
                    department,
                    totalByDept.getOrDefault(department, 0L) + 1
            );

            // 🔹 Fetch applications by student (Team 4)
            List<Map<String, Object>> applications = webClient.get()
                    .uri("http://localhost:8084/api/applications/student/{id}", studentId)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();

            boolean isPlaced = applications.stream()
                    .anyMatch(app ->
                            "SELECTED".equalsIgnoreCase(
                                    String.valueOf(((Map<?, ?>) app).get("status"))
                            )
                    );

            if (isPlaced) {
                placedByDept.put(
                        department,
                        placedByDept.getOrDefault(department, 0L) + 1
                );
            }
        }

        List<DepartmentReportDto> result = new ArrayList<>();

        for (String dept : totalByDept.keySet()) {
            result.add(new DepartmentReportDto(
                    dept,
                    totalByDept.get(dept),
                    placedByDept.getOrDefault(dept, 0L),
                    null
            ));
        }

        return result;
    }
}
