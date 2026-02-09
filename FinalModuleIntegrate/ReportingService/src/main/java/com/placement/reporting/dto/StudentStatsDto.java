package com.placement.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentStatsDto {

    private int totalApplications;
    private int shortlisted;
    private List<ApplicationHistoryDto> history;
}
