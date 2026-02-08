package com.wiley.Application_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private Long studentId;
    private Long jobId;
    private LocalDate appliedDate;
    private String status;

    // Student Details(from team2)
    private String studentName;
    private String studentDepartment;
    private Double studentCgpa;
    private String studentSkills;
    private String resumeUrl;

    // Job details (from team 3)
    private String jobTitle;
    private String companyName;
    private Double jobPackage;
    private String jobLocation;

}