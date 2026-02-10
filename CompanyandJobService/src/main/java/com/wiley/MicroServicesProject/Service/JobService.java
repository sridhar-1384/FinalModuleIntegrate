package com.wiley.MicroServicesProject.Service;

import com.wiley.MicroServicesProject.client.AuthClient;
import com.wiley.MicroServicesProject.DTO.AuthUser;
import com.wiley.MicroServicesProject.DTO.JobResponse;
import com.wiley.MicroServicesProject.Entity.Company;
import com.wiley.MicroServicesProject.Entity.Job;
import com.wiley.MicroServicesProject.Repository.CompanyRepository;
import com.wiley.MicroServicesProject.Repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AuthClient authClient;

    // Student / PO: all jobs
    public List<JobResponse> getAllJobsWithCompany() {
        List<Job> jobs = jobRepository.findAll();
        return mapToJobResponses(jobs);
    }

    // Company HR or PO create job
    public Job createJobForLoggedInCompany(String token, Job job) {
        AuthUser user = authClient.validate(token);

        if (user == null || user.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session");
        }

        // Only COMPANY_HR or PLACEMENT_OFFICER allowed
        if (!"COMPANY_HR".equals(user.getRole()) && !"PLACEMENT_OFFICER".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed");
        }

        // If COMPANY_HR: force companyId from userId mapping
        if ("COMPANY_HR".equals(user.getRole())) {
            Company company = companyRepository.findByUserId(user.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company profile not created"));

            job.setCompanyId(company.getId());
        } else {
            // If PLACEMENT_OFFICER: companyId must be provided
            if (job.getCompanyId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "companyId is required for PLACEMENT_OFFICER");
            }
        }

        job.setStatus("OPEN");
        return jobRepository.save(job);
    }

    // Company HR: view only own jobs
    public List<JobResponse> getJobsForLoggedInCompany(String token) {
        AuthUser user = authClient.validate(token);

        if (user == null || user.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session");
        }

        if (!"COMPANY_HR".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only COMPANY_HR can view my jobs");
        }

        Company company = companyRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company profile not created"));

        List<Job> jobs = jobRepository.findByCompanyId(company.getId());
        return mapToJobResponses(jobs);
    }

    private List<JobResponse> mapToJobResponses(List<Job> jobs) {
        List<JobResponse> response = new ArrayList<>();

        for (Job job : jobs) {
            JobResponse jr = new JobResponse();
            jr.setId(job.getId());
            jr.setTitle(job.getTitle());
            jr.setLocation(job.getLocation());
            jr.setPackageLpa(job.getPackageLpa());
            jr.setMinCgpa(job.getMinCgpa());
            jr.setDeadline(job.getDeadline());
            jr.setCompanyId(job.getCompanyId());
            jr.setDescription(job.getDescription());

            Company company = null;
            if (job.getCompanyId() != null) {
                company = companyRepository.findById(job.getCompanyId()).orElse(null);
            }
            jr.setCompanyName(company != null ? company.getName() : "Unknown");

            response.add(jr);
        }

        return response;
    }
    public JobResponse getJobByIdWithCompany(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Job not found"));

        Company company = null;
        if (job.getCompanyId() != null) {
            company = companyRepository.findById(job.getCompanyId()).orElse(null);
        }

        JobResponse jr = new JobResponse();
        jr.setId(job.getId());
        jr.setTitle(job.getTitle());
        jr.setLocation(job.getLocation());
        jr.setPackageLpa(job.getPackageLpa());
        jr.setMinCgpa(job.getMinCgpa());
        jr.setDeadline(job.getDeadline());
        jr.setCompanyId(job.getCompanyId());
        jr.setDescription(job.getDescription());
        jr.setCompanyName(company != null ? company.getName() : "Unknown");

        return jr;
    }

}
