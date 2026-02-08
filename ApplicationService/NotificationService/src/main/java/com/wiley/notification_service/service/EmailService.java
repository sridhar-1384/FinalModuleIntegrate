package com.wiley.notification_service.service;

import com.wiley.notification_service.dto.EmailRequestDTO;
import com.wiley.notification_service.model.EmailLog;
import com.wiley.notification_service.repository.EmailLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailRepo;

    public void sendEmail(EmailRequestDTO dto) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(dto.getRecipient());
        mail.setSubject(dto.getSubject());
        mail.setText(dto.getBody());

        EmailLog log = new EmailLog();
        log.setRecipient(dto.getRecipient());
        log.setSubject(dto.getSubject());
        log.setBody(dto.getBody());
        log.setSentDate(LocalDateTime.now());

        try {
            mailSender.send(mail);
            log.setStatus("SENT");
        } catch (Exception e) {
            log.setStatus("FAILED");
        }

        emailRepo.save(log);
    }
}
