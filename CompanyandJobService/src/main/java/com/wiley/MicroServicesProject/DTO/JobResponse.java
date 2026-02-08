package com.wiley.MicroServicesProject.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JobResponse {

    private Long id;
    private String title;
    private String location;
    private Double packageLpa;
    private Double minCgpa;
    private LocalDate deadline;
    private Long companyId;
    private String companyName;
}
