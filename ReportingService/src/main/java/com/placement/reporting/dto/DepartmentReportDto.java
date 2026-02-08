package com.placement.reporting.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentReportDto {

    private String department;

    private Long totalStudents;
    private Long placedStudents;

    private Double averagePackage;
}
