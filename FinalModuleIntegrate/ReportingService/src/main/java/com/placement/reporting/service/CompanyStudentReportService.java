package com.placement.reporting.service;

import com.lowagie.text.pdf.PdfPTable;
import com.placement.reporting.client.ApplicationClient;
import com.placement.reporting.client.CompanyClient;
import com.placement.reporting.client.StudentClient;
import com.placement.reporting.dto.ApplicationHistoryDto;
import com.placement.reporting.dto.CompanyReportDto;
import com.placement.reporting.dto.StudentOverallStatsDto;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import com.placement.reporting.dto.StudentStatsDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class CompanyStudentReportService {

    private final CompanyClient companyClient;
    private final ApplicationClient applicationClient;
    private final StudentClient studentClient;

    public CompanyStudentReportService(CompanyClient companyClient,
                                       ApplicationClient applicationClient,
                                       StudentClient studentClient) {
        this.companyClient = companyClient;
        this.applicationClient = applicationClient;
        this.studentClient = studentClient;
    }

    // =========================
    // Company-wise Report
    // =========================
    public List<CompanyReportDto> getCompanyWiseReport() {

        var companies = companyClient.getAllCompanies();
        var jobs = companyClient.getAllJobs();

        List<CompanyReportDto> result = new java.util.ArrayList<>();

        for (var company : companies) {

            var companyJobs = jobs.stream()
                    .filter(job -> job.getCompanyId().equals(company.getId()))
                    .toList();

            int jobsPosted = companyJobs.size();
            int totalHired = 0;
            double totalPackage = 0.0;

            for (var job : companyJobs) {

                var applications = applicationClient.getApplicationsByJob(job.getId());

                int hiredForJob = (int) applications.stream()
                        .filter(app -> "SELECTED".equalsIgnoreCase(app.getStatus()))
                        .count();

                totalHired += hiredForJob;

                if (hiredForJob > 0) {
                    totalPackage += job.getPackageAmount();
                }
            }

            double avgPackage = (jobsPosted > 0 && totalHired > 0)
                    ? totalPackage / jobsPosted
                    : 0.0;

            CompanyReportDto dto = new CompanyReportDto(
                    company.getName(),
                    jobsPosted,
                    totalHired,
                    avgPackage
            );

            result.add(dto);
        }

        return result;
    }

    // =========================
    // Overall Student Statistics (Admin View)
    // =========================
    public StudentOverallStatsDto getStudentOverallStats() {

        var students = studentClient.getAllStudents();
        int totalStudents = students.size();

        int appliedStudents = 0;
        int shortlistedStudents = 0;
        int selectedStudents = 0;
        int appliedNotShortlisted = 0;
        int neverApplied = 0;

        for (var student : students) {
            Long studentId = Long.valueOf(student.get("id").toString());

            var applications = applicationClient.getApplicationsByStudent(studentId);

            if (applications.isEmpty()) {
                neverApplied++;
                continue;
            }

            appliedStudents++;

            boolean hasShortlisted = applications.stream()
                    .anyMatch(app -> "SHORTLISTED".equalsIgnoreCase(app.getStatus()));

            boolean hasSelected = applications.stream()
                    .anyMatch(app -> "SELECTED".equalsIgnoreCase(app.getStatus()));

            if (hasShortlisted) {
                shortlistedStudents++;
            }

            if (hasSelected) {
                selectedStudents++;
            }

            if (!hasShortlisted && !hasSelected) {
                appliedNotShortlisted++;
            }
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
            // 1. Get company report data
            List<CompanyReportDto> reportData = getCompanyWiseReport();

            // 2. Create PDF in memory
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            // 3. Title
            document.add(new Paragraph("Company-wise Placement Report"));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // 4. Table with 4 columns
            PdfPTable table = new PdfPTable(4);
            table.addCell("Company");
            table.addCell("Jobs Posted");
            table.addCell("Students Hired");
            table.addCell("Avg Package (LPA)");

            // 5. Fill table data
            for (CompanyReportDto dto : reportData) {
                table.addCell(dto.getCompanyName());
                table.addCell(String.valueOf(dto.getJobsPosted()));
                table.addCell(String.valueOf(dto.getStudentsHired()));
                table.addCell(String.format("%.2f", dto.getAvgPackage()));
            }

            document.add(table);
            document.close();

            // 6. Return as downloadable PDF
            byte[] pdfBytes = out.toByteArray();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=company-report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public StudentStatsDto getStudentStats(Long studentId) {

        // 1. Get all applications of this student (from Application Service)
        var applications = applicationClient.getApplicationsByStudent(studentId);

        StudentStatsDto stats = new StudentStatsDto();
        stats.setTotalApplications(applications.size());

        int shortlistedCount = (int) applications.stream()
                .filter(app -> "SHORTLISTED".equalsIgnoreCase(app.getStatus()))
                .count();

        stats.setShortlisted(shortlistedCount);

        // 2. Get all jobs and companies (to map names)
        var jobs = companyClient.getAllJobs();
        var companies = companyClient.getAllCompanies();

        List<ApplicationHistoryDto> history = new java.util.ArrayList<>();

        for (var app : applications) {

            var jobOpt = jobs.stream()
                    .filter(j -> j.getId().equals(app.getJobId()))
                    .findFirst();

            if (jobOpt.isPresent()) {
                var job = jobOpt.get();

                var companyOpt = companies.stream()
                        .filter(c -> c.getId().equals(job.getCompanyId()))
                        .findFirst();

                String companyName = companyOpt.map(c -> c.getName()).orElse("Unknown");

                ApplicationHistoryDto item = new ApplicationHistoryDto(
                        companyName,
                        job.getTitle(),
                        app.getStatus()
                );

                history.add(item);
            }
        }

        stats.setHistory(history);
        return stats;
    }



}
