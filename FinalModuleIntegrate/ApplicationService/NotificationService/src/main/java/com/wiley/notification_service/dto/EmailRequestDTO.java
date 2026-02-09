package com.wiley.notification_service.dto;

import lombok.Data;

@Data
public class EmailRequestDTO {
    private String recipient;
    private String subject;
    private String body;
}

