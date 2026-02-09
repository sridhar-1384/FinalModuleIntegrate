package com.wiley.MicroServicesProject.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Table(name = "jobs")
@Getter
@Setter
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    private String title;
    private String description;

    // ✅ REQUIRED DB COLUMN NAME: package
    @Column(name = "package")
    private Double packageLpa;

    private String location;

    @Column(name = "min_cgpa")
    private Double minCgpa;

    private LocalDate deadline;
    private String status;
}