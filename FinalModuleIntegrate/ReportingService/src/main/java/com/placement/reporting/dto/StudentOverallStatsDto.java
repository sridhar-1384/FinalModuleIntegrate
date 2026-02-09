package com.placement.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentOverallStatsDto {

    private int totalStudents;
    private int appliedStudents;
    private int shortlistedStudents;
    private int selectedStudents;
    private int appliedNotShortlisted;
    private int neverApplied;
}
