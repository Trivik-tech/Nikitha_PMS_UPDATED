package com.triviktech.utilities.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Service class responsible for sending emails in the application.
 *
 * Uses Spring's JavaMailSender to send plain text emails asynchronously.
 */
@Component
public class EmailService {

    private JavaMailSender javaMailSender;

    /**
     * Constructor to initialize EmailService with JavaMailSender.
     *
     * @param javaMailSender the JavaMailSender instance used to send emails
     */
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Sends an email asynchronously to the specified recipient.
     *
     * @param to      the recipient's email address
     * @param subject the subject line of the email
     * @param message the plain text content of the email
     */
    @Async
    public void sendEmail(String to, String subject, String message) {
        try {
            SimpleMailMessage smm = new SimpleMailMessage();
            smm.setTo(to);
            smm.setSubject(subject);
            smm.setText(message);

            javaMailSender.send(smm);
        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions during email sending
        }
    }
}
