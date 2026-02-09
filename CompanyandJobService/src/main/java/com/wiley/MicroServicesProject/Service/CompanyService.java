package com.wiley.MicroServicesProject.Service;

import com.wiley.MicroServicesProject.DTO.AuthUser;
import com.wiley.MicroServicesProject.Entity.Company;
import com.wiley.MicroServicesProject.Repository.CompanyRepository;
import com.wiley.MicroServicesProject.client.AuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AuthClient authClient;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

    public Company getCompanyByUserId(Long userId) {
        return companyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

    public Company createMyCompany( Company company) {

//        AuthUser user = authClient.validate(token);
//
//        if (user == null || user.getUserId() == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session");
//        }
//
//        if (!"COMPANY_HR".equals(user.getRole())) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
//                    "Only COMPANY_HR can create company profile");
//        }
//
//        // Prevent duplicates: one userId -> one company
//        companyRepository.findByUserId(user.getUserId()).ifPresent(c -> {
//            throw new ResponseStatusException(HttpStatus.CONFLICT,
//                    "Company profile already exists for this user");
//        });
//
//        company.setUserId(user.getUserId());
        return companyRepository.save(company);
    }
}
