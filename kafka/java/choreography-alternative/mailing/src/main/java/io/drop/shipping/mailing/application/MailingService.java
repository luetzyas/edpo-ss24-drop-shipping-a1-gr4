package io.drop.shipping.mailing.application;

import org.springframework.stereotype.Component;

@Component
public class MailingService {

    public void sendMail(String subject, String body, String recipient) {
        System.out.println("Mail sent to " + recipient + " with subject " + subject + " and body " + body);
    }

}
