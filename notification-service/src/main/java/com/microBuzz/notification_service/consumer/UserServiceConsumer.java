package com.microBuzz.notification_service.consumer;

import com.microBuzz.notification_service.repository.NotificationRepository;
import com.microBuzz.notification_service.service.EmailSenderService;
import com.microBuzz.user_service.event.UserOnboardingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceConsumer {

    private final EmailSenderService emailSenderService;

    @KafkaListener(topics = "user-onboarding-topic")
    public void sendWelcomeMail(UserOnboardingEvent userOnboardingEvent){

        String subject = "Welcome to MicroBuzz - Your New Social Experience!";
        String body = String.format("Dear %s,\r\n\r\n" +
                "Welcome to microBuzz! 🎉\r\n\r\n" +
                "We’re excited to have you join our growing community. microBuzz is designed to be a place where you can share your thoughts, stay connected with your network, and stay up-to-date with what’s happening in the world—all in real-time.\r\n" +
                "Here’s what you can expect with your new account:\r\n\r\n" +
                "Post Updates: Share your thoughts and ideas with your followers.\r\n" +
                "Stay Informed: Discover trending topics and stay connected to the conversations that matter most to you.\r\n" +
                "Getting started is easy:\r\n\r\n" +
                "Customize your profile: Personalize your experience by updating your profile.\r\n" +
                "Follow people: Start following friends, influencers, or topics you're interested in.\r\n" +
                "Create your first post: Share your thoughts with the community and get involved!\r\n\r\n" +
                "We're constantly working to improve microBuzz and bring you more exciting features. If you have any questions or need help, our support team is here for you.\r\n\r\n" +
                "Thank you for joining us. We can’t wait to see what you’ll share with the community!\r\n\r\n" +
                "Best regards,\r\n" +
                "The microBuzz Team\r\n\r\n", userOnboardingEvent.getName());

        log.info("Sending welcome email to the new user: {} ", userOnboardingEvent.getName());
        emailSenderService.sendEmail(userOnboardingEvent.getEmail(), subject, body);

        log.info("Welcome mail sent successfully!");
    }
}
