package com.placement.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PoDashboardDto {
    private long totalStudents;
    private long totalJobs;
    private long totalApplications;
    private long placedStudents;
    private double averagePackage;
}
