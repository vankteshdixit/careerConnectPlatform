package com.vank.careerConnectPlatform.NotificationService.repository;

import com.vank.careerConnectPlatform.NotificationService.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Long, Notification> {

}
