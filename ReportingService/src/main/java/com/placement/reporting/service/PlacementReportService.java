package com.placement.reporting.service;

import com.placement.reporting.client.ApplicationClient;
import com.placement.reporting.client.CompanyClient;
import com.placement.reporting.client.StudentClient;
import com.placement.reporting.dto.PlacementReportDto;
import org.springframework.stereotype.Service;

@Service
public class PlacementReportService {

    private final StudentClient studentClient;
    private final ApplicationClient applicationClient;
    private final CompanyClient companyClient;

    public PlacementReportService(StudentClient studentClient,
                                  ApplicationClient applicationClient,
                                  CompanyClient companyClient) {
        this.studentClient = studentClient;
        this.applicationClient = applicationClient;
        this.companyClient = companyClient;
    }

    public PlacementReportDto getPlacementReport() {

        var students = studentClient.getAllStudents();
        var jobs = companyClient.getAllJobs();

        long totalStudents = students.size();

        long placedStudents = 0;
        double totalPackage = 0.0;
        double highestPackage = 0.0;

        for (var job : jobs) {

            var applications = applicationClient.getApplicationsByJob(job.getId());

            for (var app : applications) {
                if ("SELECTED".equalsIgnoreCase(app.getStatus())) {
                    placedStudents++;
                    double pkg = job.getPackageAmount();
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

        long companiesVisited = companyClient.getAllCompanies().size();

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
