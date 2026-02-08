package com.wiley.notification_service.service;

import com.wiley.notification_service.dto.AnnouncementRequestDTO;
import com.wiley.notification_service.model.Announcement;
import com.wiley.notification_service.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository repo;

    public Announcement create(AnnouncementRequestDTO dto, String createdBy) {
        Announcement a = new Announcement();
        a.setTitle(dto.getTitle());
        a.setMessage(dto.getMessage());
        a.setTargetRole(dto.getTargetRole());
        a.setCreatedBy(createdBy);
        a.setCreatedDate(LocalDateTime.now());
        return repo.save(a);
    }

    public List<Announcement> getAll() {
        return repo.findAll();
    }
}
