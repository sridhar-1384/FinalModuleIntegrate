package com.wiley.MicroServicesProject.Controller;


import com.wiley.MicroServicesProject.Service.StudentBrowserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class StudentBrowserController {

    private final StudentBrowserService service;

    @GetMapping("/getalljobs")
    public String getHomePage(Model model)
    {
        model.addAttribute("jobs",service.addStudentBrowserData());
        return "student-browse-jobs";
    }

}
