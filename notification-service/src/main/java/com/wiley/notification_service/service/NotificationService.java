package com.wiley.notification_service.service;

import com.wiley.notification_service.dto.NotificationRequestDTO;
import com.wiley.notification_service.model.Notification;
import com.wiley.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;

    public void sendNotification(NotificationRequestDTO dto) {
        Notification n = new Notification();
        n.setUserId(dto.getUserId());
        n.setMessage(dto.getMessage());
        n.setRead(false);
        n.setCreatedDate(LocalDateTime.now());
        repo.save(n);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return repo.findByUserIdOrderByCreatedDateDesc(userId);
    }

    public void markAsRead(Long id) {
        Notification n = repo.findById(id).orElseThrow();
        n.setRead(true);
        repo.save(n);
    }
}
