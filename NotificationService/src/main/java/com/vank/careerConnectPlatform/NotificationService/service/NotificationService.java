package com.vank.careerConnectPlatform.NotificationService.service;

import com.vank.careerConnectPlatform.NotificationService.entity.Notification;
import com.vank.careerConnectPlatform.NotificationService.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void addNotification(Notification notification){
        log.info("Adding notification to DB, content: {}", notification.getMessage());
        notification = notificationRepository.save(notification);

//        Send mailer to send an email
//        FCM

    }
}
