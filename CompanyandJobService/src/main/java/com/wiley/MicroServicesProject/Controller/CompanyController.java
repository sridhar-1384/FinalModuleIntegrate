package com.wiley.MicroServicesProject.Controller;

import com.wiley.MicroServicesProject.Entity.Company;
import com.wiley.MicroServicesProject.Service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/list")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    public Company getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id);
    }

    @GetMapping("/userId/{userId}")
    public Company getCompanyByUserId(@PathVariable("userId") Long userId) {
        return companyService.getCompanyByUserId(userId);
    }


    @PostMapping("/me")
    public Company createMyCompany(
            @RequestBody Company company) {

        return companyService.createMyCompany( company);
    }
}
