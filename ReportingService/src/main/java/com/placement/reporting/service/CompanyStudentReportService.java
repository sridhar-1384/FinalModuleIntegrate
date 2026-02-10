package com.placement.reporting.service;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.placement.reporting.dto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayOutputStream;
import java.util.*;

@Service
@SuppressWarnings("unchecked")
public class CompanyStudentReportService {

    private final WebClient webClient;

    public CompanyStudentReportService(WebClient webClient) {
        this.webClient = webClient;
    }

    // =========================
    // Company-wise Report
    // =========================
    public List<CompanyReportDto> getCompanyWiseReport() {

        List<Map<String, Object>> companies = webClient.get()
                .uri("http://localhost:8083/api/companies/list")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        List<Map<String, Object>> jobs = webClient.get()
                .uri("http://localhost:8083/api/jobs/list")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        List<CompanyReportDto> result = new ArrayList<>();

        for (Map<String, Object> company : companies) {

            Long companyId = Long.valueOf(company.get("id").toString());
            String companyName = String.valueOf(company.get("name"));

            List<Map<String, Object>> companyJobs = jobs.stream()
                    .filter(j -> companyId.equals(
                            Long.valueOf(j.get("companyId").toString())))
                    .toList();

            int jobsPosted = companyJobs.size();
            int totalHired = 0;
            double totalPackage = 0.0;

            for (Map<String, Object> job : companyJobs) {

                Long jobId = Long.valueOf(job.get("id").toString());
                double pkg = Double.parseDouble(job.get("package").toString());

                List<Map<String, Object>> applications = webClient.get()
                        .uri("http://localhost:8084/api/applications/job/{id}", jobId)
                        .retrieve()
                        .bodyToMono(List.class)
                        .block();

                int hired = (int) applications.stream()
                        .filter(a -> "SELECTED".equalsIgnoreCase(
                                String.valueOf(a.get("status"))))
                        .count();

                totalHired += hired;
                if (hired > 0) totalPackage += pkg;
            }

            double avgPackage = (jobsPosted > 0 && totalHired > 0)
                    ? totalPackage / jobsPosted
                    : 0.0;

            result.add(new CompanyReportDto(
                    companyName,
                    jobsPosted,
                    totalHired,
                    avgPackage
            ));
        }

        return result;
    }

    // =========================
    // Overall Student Statistics
    // =========================
    public StudentOverallStatsDto getStudentOverallStats() {

        List<Map<String, Object>> students = webClient.get()
                .uri("http://localhost:8082/api/students/list")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        int totalStudents = students.size();
        int appliedStudents = 0;
        int shortlistedStudents = 0;
        int selectedStudents = 0;
        int appliedNotShortlisted = 0;
        int neverApplied = 0;

        for (Map<String, Object> student : students) {

            Long studentId = Long.valueOf(student.get("id").toString());

            List<Map<String, Object>> applications = webClient.get()
                    .uri("http://localhost:8084/api/applications/student/{id}", studentId)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();

            if (applications.isEmpty()) {
                neverApplied++;
                continue;
            }

            appliedStudents++;

            boolean shortlisted = applications.stream()
                    .anyMatch(a -> "SHORTLISTED".equalsIgnoreCase(
                            String.valueOf(a.get("status"))));

            boolean selected = applications.stream()
                    .anyMatch(a -> "SELECTED".equalsIgnoreCase(
                            String.valueOf(a.get("status"))));

            if (shortlisted) shortlistedStudents++;
            if (selected) selectedStudents++;
            if (!shortlisted && !selected) appliedNotShortlisted++;
        }

        StudentOverallStatsDto dto = new StudentOverallStatsDto();
        dto.setTotalStudents(totalStudents);
        dto.setAppliedStudents(appliedStudents);
        dto.setShortlistedStudents(shortlistedStudents);
        dto.setSelectedStudents(selectedStudents);
        dto.setAppliedNotShortlisted(appliedNotShortlisted);
        dto.setNeverApplied(neverApplied);

        return dto;
    }

    // =========================
    // Export PDF Report
    // =========================
    public ResponseEntity<byte[]> exportPdfReport() {

        try {
            List<CompanyReportDto> reportData = getCompanyWiseReport();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();
            document.add(new Paragraph("Company-wise Placement Report"));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.addCell("Company");
            table.addCell("Jobs Posted");
            table.addCell("Students Hired");
            table.addCell("Avg Package (LPA)");

            for (CompanyReportDto dto : reportData) {
                table.addCell(dto.getCompanyName());
                table.addCell(String.valueOf(dto.getJobsPosted()));
                table.addCell(String.valueOf(dto.getStudentsHired()));
                table.addCell(String.format("%.2f", dto.getAvgPackage()));
            }

            document.add(table);
            document.close();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=company-report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // =========================
    // Student Stats (Individual)
    // =========================
    public StudentStatsDto getStudentStats(Long studentId) {

        List<Map<String, Object>> applications = webClient.get()
                .uri("http://localhost:8084/api/applications/student/{id}", studentId)
                .retrieve()
                .bodyToMono(List.class)
                .block();

        StudentStatsDto stats = new StudentStatsDto();
        stats.setTotalApplications(applications.size());

        int shortlisted = (int) applications.stream()
                .filter(a -> "SHORTLISTED".equalsIgnoreCase(
                        String.valueOf(a.get("status"))))
                .count();

        stats.setShortlisted(shortlisted);

        List<Map<String, Object>> jobs = webClient.get()
                .uri("http://localhost:8083/api/jobs/list")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        List<Map<String, Object>> companies = webClient.get()
                .uri("http://localhost:8083/api/companies/list")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        List<ApplicationHistoryDto> history = new ArrayList<>();

        for (Map<String, Object> app : applications) {

            Long jobId = Long.valueOf(app.get("jobId").toString());

            Map<String, Object> job = jobs.stream()
                    .filter(j -> jobId.equals(
                            Long.valueOf(j.get("id").toString())))
                    .findFirst()
                    .orElse(null);

            if (job != null) {

                Long companyId = Long.valueOf(job.get("companyId").toString());

                Map<String, Object> company = companies.stream()
                        .filter(c -> companyId.equals(
                                Long.valueOf(c.get("id").toString())))
                        .findFirst()
                        .orElse(null);

                String companyName = company != null
                        ? String.valueOf(company.get("name"))
                        : "Unknown";

                history.add(new ApplicationHistoryDto(
                        companyName,
                        String.valueOf(job.get("title")),
                        String.valueOf(app.get("status"))
                ));
            }
        }

        stats.setHistory(history);
        return stats;
    }
}
