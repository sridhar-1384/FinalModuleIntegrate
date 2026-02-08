package com.placement.reporting.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "package")
    private Double packageAmount;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}