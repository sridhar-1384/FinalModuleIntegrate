package com.placement.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationHistoryDto {

    private String companyName;
    private String jobTitle;
    private String status;
}
