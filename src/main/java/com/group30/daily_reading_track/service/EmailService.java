package com.group30.daily_reading_track.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(){
        MimeMessagePreparator preparator = new MimeMessagePreparator(){
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO, 
                new InternetAddress("2511467092@qq.com"));
                mimeMessage.setFrom(new InternetAddress("2511467092@qq.com"));
                mimeMessage.setText("Dear User, " + "Your code number is " + "123456");
            }
        };
        try{
            mailSender.send(preparator);
        }catch(MailException ex){
            System.out.println(ex.getMessage());
        }
    }

}
