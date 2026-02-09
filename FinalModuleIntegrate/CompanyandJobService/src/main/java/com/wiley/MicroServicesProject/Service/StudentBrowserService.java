package com.wiley.MicroServicesProject.Service;


import com.wiley.MicroServicesProject.DTO.StudentBrowserDTO;
import com.wiley.MicroServicesProject.Entity.Company;
import com.wiley.MicroServicesProject.Entity.Job;
import com.wiley.MicroServicesProject.Repository.CompanyRepository;
import com.wiley.MicroServicesProject.Repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentBrowserService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    public List<StudentBrowserDTO> addStudentBrowserData() {

        List<Job> jobs = jobRepository.findAll();
        List<Company> companies = companyRepository.findAll();

        if (jobs.isEmpty() || companies.isEmpty()) {
            return List.of();
        }

        Map<Long, String> companyMap = companies.stream()
                .collect(Collectors.toMap(Company::getId, Company::getName));

        return jobs.stream()
                .map(job -> StudentBrowserDTO.builder()
                        .roleName(job.getTitle())
                        .companyName(companyMap.get(job.getCompanyId()))
                        .jobId(job.getId())
                        .studentId(1L) // demo
                        .description(job.getDescription())
                        .packages(job.getPackageLpa())
                        .location(job.getLocation())
                        .min_gpa(job.getMinCgpa())
                        .deadline(job.getDeadline())
                        .build())
                .toList();
    }
}


























//
//@Service
//@RequiredArgsConstructor
//public class StudentBrowserService {
//
//    private final WebClient webClient;
//
//    //comment next line
//    private final JobRepository jobRepository;
//
//
//    public List<StudentBrowserDTO> addStudentBrowserData() {
//
//        //all jobs
//        List<Job> jobs = webClient.get()
//                .uri("http://localhost:8083/api/jobs/list")
//                .retrieve()
//                .bodyToFlux(Job.class)
//                .collectList()
//                .block();
//
//        //all companies
//        List<Company> companies = webClient.get()
//                .uri("http://localhost:8083/api/companies/list")
//                .retrieve()
//                .bodyToFlux(Company.class)
//                .collectList()
//                .block();
//
////        Object user = webClient.get()
////                .uri("http://localhost:8081/api/auth/current-user")
////                .retrieve()
////                .bodyToFlux(Object.class)
////                .blockFirst();
////
////        Long uer_id=user.getUserId();
////
////        Object student = webClient.get()
////                .uri("http://localhost:8082/api/students/userId/{user_id}")
////                .retrieve()
////                .bodyToFlux(Object.class)
////                .blockFirst();
//
//        if (jobs == null || companies == null) {
//            return List.of();
//        }
//
//        Map<Long, String> companyMap = companies.stream()
//                .collect(Collectors.toMap(Company::getId, Company::getName));
//
//        // Merge job + company
//        return jobs.stream()
//                .map(job -> {
//                    StudentBrowserDTO dto =
//                            StudentBrowserDTO.builder()
//                                    .roleName(job.getTitle())
//                                    .companyName(companyMap.get(job.getCompanyId()))
//                                    .jobId(job.getId())
////                                    .studentId(student.getId())
//                                    .studentId(1L)
//                                    .description(job.getDescription())
//                                    .packages(job.getPackageLpa())
//                                    .location(job.getLocation())
//                                    .min_gpa(job.getMinCgpa())
//                                    .deadline(job.getDeadline())
//                                    .build();
//                    return dto;
//                })
//                .toList();
//    }
//}
