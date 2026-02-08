package com.placement.reporting.service;

import com.placement.reporting.client.ApplicationClient;
import com.placement.reporting.client.CompanyClient;
import com.placement.reporting.client.StudentClient;
import com.placement.reporting.dto.PoDashboardDto;
import org.springframework.stereotype.Service;

@Service
public class PoDashboardService {

    private final StudentClient studentClient;
    private final CompanyClient companyClient;
    private final ApplicationClient applicationClient;

    public PoDashboardService(
            StudentClient studentClient,
            CompanyClient companyClient,
            ApplicationClient applicationClient) {

        this.studentClient = studentClient;
        this.companyClient = companyClient;
        this.applicationClient = applicationClient;
    }

    public PoDashboardDto getDashboardData() {

        long totalStudents = studentClient.getAllStudents().size();
        long totalJobs = companyClient.getAllJobs().size();

        long totalApplications = 0;
        long placedStudents = 0;

        double totalPackage = 0.0;
        long selectedCount = 0;

        var jobs = companyClient.getAllJobs();

        for (var job : jobs) {

            var applications = applicationClient.getApplicationsByJob(job.getId());
            totalApplications += applications.size();

            for (var app : applications) {
                if ("SELECTED".equalsIgnoreCase(app.getStatus())) {
                    placedStudents++;
                    totalPackage += job.getPackageAmount();
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
