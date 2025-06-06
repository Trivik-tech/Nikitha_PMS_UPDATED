package com.triviktech.utilities.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(String to, String subject,String message){
        try{
            SimpleMailMessage smm=new SimpleMailMessage();
            smm.setTo(to);
            smm.setSubject(subject);
            smm.setText(message);

            javaMailSender.send(smm);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
