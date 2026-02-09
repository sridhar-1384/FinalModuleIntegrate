package com.wiley.notification_service.controller;

import com.wiley.notification_service.dto.EmailRequestDTO;
import com.wiley.notification_service.dto.NotificationRequestDTO;
import com.wiley.notification_service.model.Notification;
import com.wiley.notification_service.service.EmailService;
import com.wiley.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin("*")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;
    private final EmailService emailService;

    @PostMapping("/notification")
    public void send(@RequestBody NotificationRequestDTO dto) {
        service.sendNotification(dto);
    }

    @GetMapping("/user/{id}")
    public List<Notification> getUser(@PathVariable Long id) {
        return service.getUserNotifications(id);
    }

    @PutMapping("/{id}/read")
    public void markRead(@PathVariable Long id) {
        service.markAsRead(id);
    }

    @PostMapping("/email")
    public void sendEmail(@RequestBody EmailRequestDTO dto) {
        emailService.sendEmail(dto);
    }
}
