package com.wiley.MicroServicesProject.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentBrowserDTO {

    private String roleName;
    private String companyName;
    private Long studentId;
    private Long jobId;

    private String description;
    private double packages;
    private String location;
    private double min_gpa;
    private LocalDate deadline;

}
