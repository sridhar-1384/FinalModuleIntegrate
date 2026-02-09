package com.placement.reporting.service;

import com.placement.reporting.dto.PlacementReportDto;
import com.placement.reporting.repository.PlacementReportRepository;
import org.springframework.stereotype.Service;

@Service
public class PlacementReportService {

    private final PlacementReportRepository repository;

    public PlacementReportService(PlacementReportRepository repository) {
        this.repository = repository;
    }

    public PlacementReportDto getPlacementReport() {

        Long totalStudents = repository.countTotalStudents();
        Long placedStudents = repository.countPlacedStudents();
        Double avgPackage = repository.findAveragePackage();
        Double highestPackage = repository.findHighestPackage();
        Long companiesVisited = repository.countCompaniesVisited();

        double placedPercentage = 0.0;
        if (totalStudents != null && totalStudents > 0 && placedStudents != null) {
            placedPercentage = (placedStudents * 100.0) / totalStudents;
        }

        return new PlacementReportDto(
                totalStudents,
                placedStudents,
                placedPercentage,
                avgPackage,
                highestPackage,
                companiesVisited
        );
    }
}
