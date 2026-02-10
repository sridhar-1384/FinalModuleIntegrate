package com.wiley.MicroServicesProject.Controller;

import com.wiley.MicroServicesProject.DTO.JobResponse;
import com.wiley.MicroServicesProject.Entity.Job;
import com.wiley.MicroServicesProject.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin
public class JobController {

    @Autowired
    private JobService jobService;

    // Student / PO: list all jobs with company name
    @GetMapping("/list")    //base url
    public List<JobResponse> getAllJobs() {
        return jobService.getAllJobsWithCompany();
    }

    // Student / PO: get a single job with company name
    @GetMapping("/{id}")
    public JobResponse getJobById(@PathVariable Long id) {
        return jobService.getJobByIdWithCompany(id);
    }

    // Company HR / PO: create job
    @PostMapping("/create")
    public Job createJob(
            @RequestHeader("Session-Token") String token,
            @RequestBody Job job) {
        return jobService.createJobForLoggedInCompany(token, job);
    }

    // Company HR: list only my jobs
    @GetMapping("/my")
    public List<JobResponse> myJobs(@RequestHeader("Session-Token") String token) {
        return jobService.getJobsForLoggedInCompany(token);
    }
}
