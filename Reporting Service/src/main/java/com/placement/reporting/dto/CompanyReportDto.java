package com.placement.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyReportDto {

    private String companyName;
    private int jobsPosted;
    private int studentsHired;
    private double avgPackage;

}
