package com.wiley.notification_service.dto;

import lombok.Data;

@Data
public class NotificationRequestDTO {
    private Long userId;
    private String message;
    //private String type;
}

