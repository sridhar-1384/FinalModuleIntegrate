package com.wiley.notification_service.controller;

import com.wiley.notification_service.dto.AnnouncementRequestDTO;
import com.wiley.notification_service.model.Announcement;
import com.wiley.notification_service.service.AnnouncementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AnnouncementController {

    private final AnnouncementService service;

    @PostMapping("/create")
    public Announcement create(@RequestBody AnnouncementRequestDTO dto,
                               HttpServletRequest request) {

        // TEMP username (replace later with JWT)
        String createdBy = "SYSTEM";

        return service.create(dto, createdBy);
    }

    @GetMapping("/list")
    public List<Announcement> list() {
        return service.getAll();
    }
}
