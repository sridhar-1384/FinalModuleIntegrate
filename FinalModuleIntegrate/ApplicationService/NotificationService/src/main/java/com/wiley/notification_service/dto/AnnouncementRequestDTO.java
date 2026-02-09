package com.wiley.notification_service.dto;

import lombok.Data;

@Data
public class AnnouncementRequestDTO {
    private String title;
    private String message;
    private String targetRole;
    private boolean sendEmail;
}
