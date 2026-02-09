package com.wiley.notification_service.repository;

import com.wiley.notification_service.model.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
}

