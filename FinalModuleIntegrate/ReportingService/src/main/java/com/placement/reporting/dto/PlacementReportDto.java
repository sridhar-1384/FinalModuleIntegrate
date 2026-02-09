package com.placement.reporting.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlacementReportDto {

    private Long totalStudents;
    private Long placedStudents;
    private Double placedPercentage;

    private Double averagePackage;
    private Double highestPackage;

    private Long companiesVisited;
}
