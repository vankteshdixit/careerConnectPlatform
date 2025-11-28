package com.vank.careerConnectPlatform.NotificationService.consumer;

import com.vank.careerConnectPlatform.NotificationService.entity.Notification;
import com.vank.careerConnectPlatform.NotificationService.service.NotificationService;
import com.vank.careerConnectPlatform.postsService.event.PostCreated;
import com.vank.careerConnectPlatform.postsService.event.PostLiked;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "post_created_topic")
    public void handlePostCreated(PostCreated postCreated){
        log.info("received notification: {}", postCreated);

        String message = String.format("Your connection with ID: %d has created this post: %s",
                postCreated.getOwnerUserId(), postCreated.getContent());

        Notification notification = Notification.builder()
                .message(message)
                .userId(postCreated.getUserId())
                .build();
        notificationService.addNotification(notification);
    }

    @KafkaListener(topics = "post_liked_topic")
    public void handlePostLiked(PostLiked postLiked){
        log.info("handlePostLiked: {}", postLiked);

        String message = String.format("User with id: %d has liked your post with id: %d",
                postLiked.getLikedByUserId(), postLiked.getPostId());

        Notification notification = Notification.builder()
                .message(message)
                .userId(postLiked.getOwnerUserId())
                .build();
        notificationService.addNotification(notification);
    }
}
