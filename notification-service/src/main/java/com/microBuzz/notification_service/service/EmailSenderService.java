package com.microBuzz.notification_service.service;

public interface EmailSenderService {

    void sendEmail (String toEmail, String subject, String body);

}
