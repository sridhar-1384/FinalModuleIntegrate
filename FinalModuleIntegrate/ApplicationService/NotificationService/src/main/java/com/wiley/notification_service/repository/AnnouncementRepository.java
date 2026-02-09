package com.wiley.notification_service.repository;

import com.wiley.notification_service.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
}

