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

import javax.sound.midi.SysexMessage;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final WebClient.Builder webClientBuilder;

    private static final String AUTH_SERVICE_URL = "http://localhost:8081/api/auth";
    private static final String STUDENT_SERVICE_URL = "http://localhost:8082";
    private static final String JOB_SERVICE_URL = "http://localhost:8083";
    private static final String COMPANY_SERVICE_URL = "http://localhost:8083";

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

            if (authResponse != null) return authResponse;
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Session");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error in authorization");
        }
    }

    @Transactional
    public ApplicationResponse applyToJob(String token, ApplicationRequest request) {
        Map<String, Object> auth = validateSession(token);
        String role = (String) auth.get("role");
        long userId = ((Number) auth.get("userId")).longValue();

        if (!"STUDENT".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only Students can apply.");
        }

        Map<String, Object> studentResponse = getStudentByUserId(userId, token);
        Long realStudentId = ((Number) studentResponse.get("id")).longValue();

        if (!realStudentId.equals(request.getStudentId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ID Mismatch.");
        }

        if (applicationRepository.existsByStudentIdAndJobId(request.getStudentId(), request.getJobId())) {
            throw new RuntimeException("Already applied.");
        }

        Application application = new Application();
        application.setStudentId(request.getStudentId());
        application.setJobId(request.getJobId());
        application.setAppliedDate(LocalDate.now());
        application.setStatus("APPLIED");

        return buildApplicationResponse(applicationRepository.save(application), token);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getStudentApplications(String token, Long studentId) {
        Map<String, Object> auth = validateSession(token);
        String role = (String) auth.get("role");

        if ("STUDENT".equalsIgnoreCase(role)) {
            long userId = ((Number) auth.get("userId")).longValue();
            Map<String, Object> studentResponse = getStudentByUserId(userId, token);
            Long realStudentId = ((Number) studentResponse.get("id")).longValue();
            if (!realStudentId.equals(studentId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied.");
            }
        }

        List<Application> applications = applicationRepository.findByStudentId(studentId);
        List<ApplicationResponse> responses = new ArrayList<>();
        for (Application app : applications) responses.add(buildApplicationResponse(app, token));
        return responses;
    }

    @Transactional(readOnly = true)
    public ApplicationResponse getApplicationDetails(String token, Long id) {
        Map<String, Object> auth = validateSession(token);
        String role = (String) auth.get("role");
        long userId = ((Number) auth.get("userId")).longValue();

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));

        if ("STUDENT".equalsIgnoreCase(role)) {
            Map<String, Object> studentResponse = getStudentByUserId(userId, token);
            Long realStudentId = ((Number) studentResponse.get("id")).longValue();
            if (!application.getStudentId().equals(realStudentId)){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your application.");
            }
        }

        if ("COMPANY_HR".equalsIgnoreCase(role)) {
            Map<String, Object> companyResponse = getCompanyById(userId, token);
            verifyJobOwner(application.getJobId(), (Number) companyResponse.get("id"));
        }

        return buildApplicationResponse(application, token);
    }

    @Transactional
    public ApplicationResponse updateStatus(String token, Long id, StatusUpdateRequest request) {
        Map<String, Object> auth = validateSession(token);
        String role = (String) auth.get("role");

        if (!"COMPANY_HR".equalsIgnoreCase(role) && !"PLACEMENT_OFFICER".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied.");
        }

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));

        if ("COMPANY_HR".equalsIgnoreCase(role)) {
            long userId = ((Number) auth.get("userId")).longValue();
            Map<String, Object> companyResponse = getCompanyById(userId, token);
            verifyJobOwner(application.getJobId(), (Number) companyResponse.get("id"));
        }

        application.setStatus(request.getStatus());
        return buildApplicationResponse(applicationRepository.save(application), token);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getJobApplicants(String token, Long jobId) {
        validateSession(token); // Ensure logged in
        List<Application> applications = applicationRepository.findByJobId(jobId);
        List<ApplicationResponse> responses = new ArrayList<>();
        for (Application app : applications) responses.add(buildApplicationResponse(app, token));
        return responses;
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getAllApplications(String token) {
        Map<String, Object> auth = validateSession(token);
        if (!"PLACEMENT_OFFICER".equalsIgnoreCase((String) auth.get("role"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only PO access.");
        }
        List<ApplicationResponse> responses = new ArrayList<>();
        for (Application app : applicationRepository.findAll())
            responses.add(buildApplicationResponse(app, token));
        return responses;
    }

    private void verifyJobOwner(Long jobId, Number authCompanyId) {
        try {
            Map<String, Object> job = webClientBuilder.build().get()
                    .uri(JOB_SERVICE_URL + "/api/jobs/" + jobId)
                    .retrieve().bodyToMono(Map.class).block();

            if (job != null) {
                Long jobCompanyId = ((Number) job.get("companyId")).longValue();
                if (!jobCompanyId.equals(authCompanyId.longValue())) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized Company.");
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Verification failed.");
        }
    }

    private ApplicationResponse buildApplicationResponse(Application application, String token) {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(application.getId());
        response.setStudentId(application.getStudentId());
        response.setJobId(application.getJobId());
        response.setAppliedDate(application.getAppliedDate());
        response.setStatus(application.getStatus());
        System.out.println(application);
        System.out.println(token);

        try {
            Map<String, Object> student = webClientBuilder.build().get()
                    .uri(STUDENT_SERVICE_URL + "/api/students/" + application.getStudentId())
                    .header("SESSION-TOKEN", token)
                    .retrieve().bodyToMono(Map.class).block();
            System.out.println(student);
            if (student != null) {
                response.setStudentName((String) student.get("name"));
                response.setStudentDepartment((String) student.get("dept"));
                if (student.get("cgpa") != null) response.setStudentCgpa(((Number) student.get("cgpa")).doubleValue());
                Object skillsObj = student.get("skills");
//                if (skillsObj instanceof List)
//                response.setStudentSkills(String.join(",", (List<String>) skillsObj));
                if (skillsObj instanceof List) {
                    List<Map<String, Object>> skillsList = (List<Map<String, Object>>) skillsObj;
                    String joinedSkills = skillsList.stream()
                            .map(s -> (String) s.get("skillName"))
                            .collect(Collectors.joining(", "));
                    response.setStudentSkills(joinedSkills);
                }
                if (student.get("resumePath") != null) {
                    response.setResumeUrl(STUDENT_SERVICE_URL + "/api/students/" + application.getStudentId() + "/resume");
                }
            }
        } catch (Exception e) {
            response.setStudentName("N/A");
        }

        try {
            Map<String, Object> job = webClientBuilder.build().get()
                    .uri(JOB_SERVICE_URL + "/api/jobs/" + application.getJobId())
                    .header("SESSION-TOKEN", token)
                    .retrieve().bodyToMono(Map.class).block();

            if (job != null) {
                response.setJobTitle((String) job.get("title"));
                response.setJobLocation((String) job.get("location"));
                if (job.get("packageLpa") != null) response.setJobPackage(((Number) job.get("packageLpa")).doubleValue());

                Long companyId = ((Number) job.get("companyId")).longValue();
                Map<String, Object> company = webClientBuilder.build().get()
                        .uri(COMPANY_SERVICE_URL + "/api/companies/" + companyId)
                        .retrieve().bodyToMono(Map.class).block();
                if (company != null) response.setCompanyName((String) company.get("name"));
            }
        } catch (Exception e) { response.setJobTitle("Unavailable"); }
        return response;
    }

    private Map<String, Object> getStudentByUserId(Long userId, String token) {
        return webClientBuilder.build().get()
                .uri(STUDENT_SERVICE_URL + "/api/students/userId/" + userId)
                .header("SESSION-TOKEN", token)
                .retrieve().bodyToMono(Map.class).block();
    }

    private Map<String, Object> getCompanyById(Long userId, String token) {
        return webClientBuilder.build().get()
                .uri(COMPANY_SERVICE_URL + "/api/companies/userId/" + userId)
                .header("SESSION-TOKEN", token)
                .retrieve().bodyToMono(Map.class).block();
    }
}
