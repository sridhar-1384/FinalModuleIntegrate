package com.wiley.Application_Service.controller;//package com.wiley.Application_Service.controller;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/mock/api")
//public class MockController {
//
//    private static final Map<Long, Map<String, Object>> students = new HashMap<>();
//    private static final Map<Long, Map<String, Object>> jobs = new HashMap<>();
//    private static final Map<Long, Map<String, Object>> companies = new HashMap<>();
//
//    static {
//        // --- Mock Students ---
//        Map<String, Object> s1 = new HashMap<>();
//        s1.put("id", 1L); s1.put("name", "Rahul Sharma"); s1.put("email", "rahul@college.edu");
//        s1.put("department", "Computer Science"); s1.put("cgpa", 8.5); s1.put("phone", "+91 9876543210");
//        s1.put("skills", "Java, Spring Boot, React");
//        s1.put("resumePath", "uploads/resume_1.pdf"); // HAS RESUME
//        students.put(1L, s1);
//
//        Map<String, Object> s2 = new HashMap<>();
//        s2.put("id", 2L); s2.put("name", "Priya Singh"); s2.put("email", "priya@college.edu");
//        s2.put("department", "Electronics"); s2.put("cgpa", 9.0); s2.put("phone", "+91 9876543211");
//        s2.put("skills", "Embedded C, IoT, Python");
//        s2.put("resumePath", "uploads/resume_2.pdf"); // HAS RESUME
//        students.put(2L, s2);
//
//        Map<String, Object> s3 = new HashMap<>();
//        s3.put("id", 3L); s3.put("name", "Amit Kumar"); s3.put("email", "amit@college.edu");
//        s3.put("department", "Mechanical"); s3.put("cgpa", 7.8); s3.put("phone", "+91 9876543212");
//        // NO SKILLS SET
//        // NO RESUME PATH SET (Simulates missing file)
//        students.put(3L, s3);
//
//        // --- Mock Companies ---
//        Map<String, Object> c1 = new HashMap<>();
//        c1.put("id", 1L); c1.put("name", "TCS"); c1.put("hrName", "Priya Kapoor"); c1.put("hrEmail", "hr@tcs.com");
//        companies.put(1L, c1);
//
//        Map<String, Object> c2 = new HashMap<>();
//        c2.put("id", 2L); c2.put("name", "Amazon"); c2.put("hrName", "John Doe"); c2.put("hrEmail", "hr@amazon.com");
//        companies.put(2L, c2);
//
//        Map<String, Object> c3 = new HashMap<>();
//        c3.put("id", 3L); c3.put("name", "Wipro"); c3.put("hrName", "Jane Smith"); c3.put("hrEmail", "hr@wipro.com");
//        companies.put(3L, c3);
//
//        // --- Mock Jobs ---
//        Map<String, Object> j1 = new HashMap<>();
//        j1.put("id", 1L); j1.put("companyId", 1L); j1.put("title", "Software Engineer"); j1.put("description", "Java Dev"); j1.put("package", 3.5); j1.put("location", "Mumbai"); j1.put("minCgpa", 7.0);
//        jobs.put(1L, j1);
//
//        Map<String, Object> j2 = new HashMap<>();
//        j2.put("id", 2L); j2.put("companyId", 2L); j2.put("title", "SDE-1"); j2.put("description", "Backend Dev"); j2.put("package", 12.0); j2.put("location", "Bangalore"); j2.put("minCgpa", 8.0);
//        jobs.put(2L, j2);
//
//        Map<String, Object> j3 = new HashMap<>();
//        j3.put("id", 3L); j3.put("companyId", 3L); j3.put("title", "Systems Engineer"); j3.put("description", "Core Infra"); j3.put("package", 4.0); j3.put("location", "Pune"); j3.put("minCgpa", 6.5);
//        jobs.put(3L, j3);
//    }
//
//    @GetMapping("/students/{id}")
//    public ResponseEntity<Map<String, Object>> getMockStudent(@PathVariable Long id) {
//        if (students.containsKey(id)) {
//            return ResponseEntity.ok(students.get(id));
//        }
//        return ResponseEntity.status(404).body(null);
//    }
//
//    @GetMapping("/jobs/{id}")
//    public ResponseEntity<Map<String, Object>> getMockJob(@PathVariable Long id) {
//        if (jobs.containsKey(id)) {
//            return ResponseEntity.ok(jobs.get(id));
//        }
//        return ResponseEntity.status(404).body(null);
//    }
//
//    @GetMapping("/companies/{id}")
//    public ResponseEntity<Map<String, Object>> getMockCompany(@PathVariable Long id) {
//        if (companies.containsKey(id)) {
//            return ResponseEntity.ok(companies.get(id));
//        }
//        return ResponseEntity.status(404).body(null);
//    }
//
//    @GetMapping("/students/{id}/resume")
//    public ResponseEntity<byte[]> getMockResume(@PathVariable Long id) {
//        String pdfContent = "%PDF-1.4\n1 0 obj\n<< /Type /Catalog /Outlines 2 0 R /Pages 3 0 R >>\nendobj\n2 0 obj\n<< /Type /Outlines /Count 0 >>\nendobj\n3 0 obj\n<< /Type /Pages /Kids [4 0 R] /Count 1 >>\nendobj\n4 0 obj\n<< /Type /Page /Parent 3 0 R /MediaBox [0 0 612 792] /Contents 5 0 R /Resources << /ProcSet 6 0 R >> >>\nendobj\n5 0 obj\n<< /Length 58 >>\nstream\nBT /F1 24 Tf 50 700 Td (Mock Resume for Student ID: " + id + ") Tj ET\nendstream\nendobj\n6 0 obj\n[ /PDF /Text ]\nendobj\nxref\n0 7\n0000000000 65535 f\n0000000009 00000 n\n0000000074 00000 n\n0000000120 00000 n\n0000000179 00000 n\n0000000300 00000 n\n0000000407 00000 n\ntrailer\n<< /Size 7 /Root 1 0 R >>\nstartxref\n436\n%%EOF";
//        byte[] pdfBytes = pdfContent.getBytes();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("attachment", "student_" + id + "_resume.pdf");
//        return ResponseEntity.ok().headers(headers).body(pdfBytes);
//    }
//}