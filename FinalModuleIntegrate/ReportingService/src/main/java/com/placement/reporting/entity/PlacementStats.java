package com.placement.reporting.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "placement_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlacementStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "total_students")
    private Integer totalStudents;

    @Column(name = "placed_students")
    private Integer placedStudents;

    @Column(name = "avg_package")
    private Double avgPackage;
}
