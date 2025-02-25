package com.microBuzz.notification_service.consumer;

import com.microBuzz.notification_service.clients.ConnectionsClient;
import com.microBuzz.notification_service.dto.PersonDto;
import com.microBuzz.notification_service.entity.Notification;
import com.microBuzz.notification_service.entity.Type;
import com.microBuzz.notification_service.repository.NotificationRepository;
import com.microBuzz.post_service.event.PostCreatedEvent;
import com.microBuzz.post_service.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceConsumer {

    private final ConnectionsClient connectionsClient;
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent){

        try {
            log.info("Sending notifications: handlePostCreated: {}", postCreatedEvent);
            List<PersonDto> firstDegreeConnections = connectionsClient.getMyFirstConnections(postCreatedEvent.getCreatorId());

            for (PersonDto connection : firstDegreeConnections) {
                log.info("Sending notification for user, Current user is: {}", connection.getUserId());
                sendNotification(connection.getUserId(), "Your connection " + postCreatedEvent.getCreatorId() + " posted something "
                        + " check it out!", Type.POST);

            }
        } catch (Exception ex) {
            log.error("Exception occurred in handlePostCreated , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in handlePostCreated: "+ex.getMessage());
        }

    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent){

        try {
            log.info("Sending notifications: handlePostLiked: {}", postLikedEvent);
            String message = String.format("Your post, %d has been liked by %d", postLikedEvent.getPostId(), postLikedEvent.getLikedByUserId());
            sendNotification(postLikedEvent.getCreatorId(), message, Type.LIKE);
            log.info("Notification sent for user: {}", postLikedEvent.getCreatorId());
        } catch (Exception ex) {
            log.error("Exception occurred in handlePostLiked , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in handlePostLiked: "+ex.getMessage());
        }

    }

    public void sendNotification(Long userId, String message, Type category){

        try {
            Notification notification = new Notification();

            notification.setUserId(userId);
            notification.setMessage(message);
            notification.setCategory(category);

            notificationRepository.save(notification);
            log.info("Successfully saved the notification for user with id {}", userId);
        } catch (Exception ex) {
            log.error("Exception occurred in sendNotification , Error Msg: {}", ex.getMessage());
            throw new RuntimeException("Exception occurred in sendNotification: "+ex.getMessage());
        }
    }
}
