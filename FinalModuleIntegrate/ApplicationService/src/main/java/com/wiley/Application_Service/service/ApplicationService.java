package com.wiley.Application_Service.service;

import com.wiley.Application_Service.dto.ApplicationRequest;
import com.wiley.Application_Service.dto.ApplicationResponse;
import com.wiley.Application_Service.dto.StatusUpdateRequest;
import com.wiley.Application_Service.entity.Application;
import com.wiley.Application_Service.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final WebClient.Builder webClientBuilder;

    // --- MOCK URLS ---
    private static final String AUTH_SERVICE_URL = "http://localhost:8084/api/auth";
    private static final String STUDENT_SERVICE_URL = "http://localhost:8084/mock";
    private static final String JOB_SERVICE_URL = "http://localhost:8084/mock";
    private static final String COMPANY_SERVICE_URL = "http://localhost:8084/mock";

//    private static final String AUTH_SERVICE_URL = "http://localhost:8081/api/auth";
//    private static final String STUDENT_SERVICE_URL = "http://localhost:8082";
//    private static final String JOB_SERVICE_URL = "http://localhost:8083";
//    private static final String COMPANY_SERVICE_URL = "http://localhost:8083";


    // session validation (userId and role)
    private Map<String, Object> validateSession(String token) {
        try {
            Map<String, Object> authResponse = webClientBuilder.build()
                    .get()
                    .uri(AUTH_SERVICE_URL + "/validate")
                    .header("SESSION-TOKEN", token)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(3))
                    .block();

            if (authResponse != null) {
                return authResponse;
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Role not found in session");

        } catch (Exception e) {
            // for Mock Testing
            if (token.startsWith("hr-1")) return Map.of("role", "COMPANY_HR", "companyId", 1L, "userId", 1L);
            if (token.startsWith("hr-2")) return Map.of("role", "COMPANY_HR", "companyId", 2L, "userId", 2L);
            if (token.startsWith("po-")) return Map.of("role", "PLACEMENT_OFFICER", "userId", 999L);
            if (token.startsWith("student-2")) return Map.of("role", "STUDENT", "userId", 2L);
            return Map.of("role", "STUDENT", "userId", 1L);

            // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error in authorization");
        }
    }

    @Transactional
    public ApplicationResponse applyToJob(String token, ApplicationRequest request) {
        Map<String, Object> auth = validateSession(token);
        String role = (String) auth.get("role");
        Long userId = ((Number) auth.get("userId")).longValue();

        if (!"STUDENT".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only Students can apply to jobs.");
        }

        if (!userId.equals(request.getStudentId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot apply on behalf of another student.");
        }

        if (applicationRepository.existsByStudentIdAndJobId(request.getStudentId(), request.getJobId())) {
            throw new RuntimeException("You have already applied to this job");
        }

        Application application = new Application();
        application.setStudentId(request.getStudentId());
        application.setJobId(request.getJobId());
        application.setAppliedDate(LocalDate.now());
        application.setStatus("APPLIED");

        Application savedApplication = applicationRepository.save(application);
        return buildApplicationResponse(savedApplication);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getStudentApplications(String token, Long studentId) {
        Map<String, Object> auth = validateSession(token);
        String role = (String) auth.get("role");
        Long userId = ((Number) auth.get("userId")).longValue();

        // Student ID Check
        if ("STUDENT".equalsIgnoreCase(role)) {
            if (!userId.equals(studentId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view another student's applications.");
            }
        }

        List<Application> applications = applicationRepository.findByStudentId(studentId);
        List<ApplicationResponse> responses = new ArrayList<>();
        for (Application app : applications) {
            responses.add(buildApplicationResponse(app));
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getJobApplicants(String token, Long jobId) {
        Map<String, Object> auth = validateSession(token);
        String role = (String) auth.get("role");

        if (!"COMPANY_HR".equalsIgnoreCase(role) && !"PLACEMENT_OFFICER".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied.");
        }

        // Company ID Check
        if ("COMPANY_HR".equalsIgnoreCase(role)) {
            verifyJobOwner(jobId, (Number) auth.get("companyId"));
        }

        List<Application> applications = applicationRepository.findByJobId(jobId);
        List<ApplicationResponse> responses = new ArrayList<>();
        for (Application app : applications) {
            responses.add(buildApplicationResponse(app));
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public ApplicationResponse getApplicationDetails(String token, Long id) {
        Map<String, Object> auth = validateSession(token);
        String role = (String) auth.get("role");
        Long userId = ((Number) auth.get("userId")).longValue();

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if ("STUDENT".equalsIgnoreCase(role)) {
            if (!application.getStudentId().equals(userId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot view details of another student's application.");
            }
        }

        if ("COMPANY_HR".equalsIgnoreCase(role)) {
            verifyJobOwner(application.getJobId(), (Number) auth.get("companyId"));
        }

        return buildApplicationResponse(application);
    }
    @Transactional
    public ApplicationResponse updateStatus(String token, Long id, StatusUpdateRequest request) {
        Map<String, Object> auth = validateSession(token);
        String role = (String) auth.get("role");

        if (!"COMPANY_HR".equalsIgnoreCase(role) && !"PLACEMENT_OFFICER".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied.");
        }

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if ("COMPANY_HR".equalsIgnoreCase(role)) {
            verifyJobOwner(application.getJobId(), (Number) auth.get("companyId"));
        }

        application.setStatus(request.getStatus());
        Application updatedApplication = applicationRepository.save(application);
        return buildApplicationResponse(updatedApplication);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getAllApplications(String token) {
        Map<String, Object> auth = validateSession(token);
        String role = (String) auth.get("role");

        if (!"PLACEMENT_OFFICER".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied: Only PO can view all records.");
        }

        List<Application> applications = applicationRepository.findAll();
        List<ApplicationResponse> responses = new ArrayList<>();
        for (Application app : applications) {
            responses.add(buildApplicationResponse(app));
        }
        return responses;
    }

    // verify Company Owner
    private void verifyJobOwner(Long jobId, Number authCompanyId) {
        try {
            Map<String, Object> job = webClientBuilder.build()
                    .get()
                    .uri(JOB_SERVICE_URL + "/api/jobs/" + jobId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (job != null) {
                Long jobCompanyId = ((Number) job.get("companyId")).longValue();
                Long hrCompanyId = authCompanyId.longValue();

                if (!jobCompanyId.equals(hrCompanyId)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access data for another company's job.");
                }
            }
        } catch (Exception e) {
            System.err.println("Ownership Check Failed: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to verify job ownership.");
        }
    }

    private ApplicationResponse buildApplicationResponse(Application application) {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(application.getId());
        response.setStudentId(application.getStudentId());
        response.setJobId(application.getJobId());
        response.setAppliedDate(application.getAppliedDate());
        response.setStatus(application.getStatus());

        try {
            Map<String, Object> student = webClientBuilder.build()
                    .get()
                    .uri(STUDENT_SERVICE_URL + "/api/students/" + application.getStudentId())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (student != null) {
                response.setStudentName((String) student.get("name"));
                response.setStudentDepartment((String) student.get("department"));
                Object cgpaObj = student.get("cgpa");
                if (cgpaObj != null) response.setStudentCgpa(((Number) cgpaObj).doubleValue());
                Object skillsObj = student.get("skills");
                if (skillsObj != null) response.setStudentSkills(skillsObj.toString());
                Object resumePath = student.get("resumePath");
                if (resumePath != null && !resumePath.toString().isEmpty()) {
                    response.setResumeUrl(STUDENT_SERVICE_URL + "/api/students/" + application.getStudentId() + "/resume");
                }
            }
        } catch (Exception e) {
            response.setStudentName("N/A");
        }

        try {
            Map<String, Object> job = webClientBuilder.build()
                    .get()
                    .uri(JOB_SERVICE_URL+"/api/jobs/"+application.getJobId())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (job != null) {
                response.setJobTitle((String) job.get("title"));
                response.setJobLocation((String) job.get("location"));
                Object packageObj = job.get("package");
                if (packageObj != null)
                    response.setJobPackage(((Number) packageObj).doubleValue());

                Object companyIdObj = job.get("companyId");
                if (companyIdObj != null) {
                    Long companyId = ((Number) companyIdObj).longValue();
                    Map<String, Object> company = webClientBuilder.build()
                            .get()
                            .uri(COMPANY_SERVICE_URL + "/api/companies/"+companyId)
                            .retrieve()
                            .bodyToMono(Map.class)
                            .block();
                    if (company != null) response.setCompanyName((String) company.get("name"));
                }
            }
        } catch (Exception e) {
            response.setJobTitle("Job Details Unavailable");
            response.setCompanyName("N/A");
            response.setJobLocation("N/A");
            response.setJobPackage(0.0);
        }

        return response;
    }
}