package com.placement.reporting.service;

import com.placement.reporting.client.ApplicationClient;
import com.placement.reporting.client.StudentClient;
import com.placement.reporting.dto.DepartmentReportDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DepartmentReportService {

    private final StudentClient studentClient;
    private final ApplicationClient applicationClient;

    public DepartmentReportService(StudentClient studentClient,
                                   ApplicationClient applicationClient) {
        this.studentClient = studentClient;
        this.applicationClient = applicationClient;
    }

    public List<DepartmentReportDto> getDepartmentReport() {

        var students = studentClient.getAllStudents();


        Map<String, Long> totalByDept = new HashMap<>();
        Map<String, Long> placedByDept = new HashMap<>();

        for (var student : students) {
            System.out.println(student);

            String department = String.valueOf(student.get("dept"));
            System.out.println("department :"+department);
            Long studentId = Long.valueOf(student.get("id").toString());
            System.out.println("id of student:"+studentId);

            totalByDept.put(
                    department,
                    totalByDept.getOrDefault(department, 0L) + 1
            );

            var applications = applicationClient.getApplicationsByStudent(studentId);

            boolean isPlaced = applications.stream()
                    .anyMatch(app -> "SELECTED".equalsIgnoreCase(app.getStatus()));

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
