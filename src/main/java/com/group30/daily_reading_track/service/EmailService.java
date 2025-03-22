package com.group30.daily_reading_track.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private Map<String, String> verificationCodes = new HashMap<>();

    public String sendVerificationCodeEmail(String email) {
        String code = generateVerificationCode();
        verificationCodes.put(email, code);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setFrom("2511467092@qq.com"); // 替换为实际发件人邮箱
            helper.setSubject("注册验证码");
            helper.setText("Dear User, Your code number is " + code);
            mailSender.send(message);
            return code;
        } catch (MessagingException | MailException ex) {
            System.out.println(ex.getMessage());
            verificationCodes.remove(email);
            return null;
        }
    }

    // 新增：找回密码验证码发送（邮件主题和内容不同）
    public String sendForgotPasswordCodeEmail(String email) {
        String code = generateVerificationCode();
        verificationCodes.put(email, code);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setFrom("2511467092@qq.com");
            helper.setSubject("找回密码验证码");
            helper.setText("亲爱的用户，您的找回密码验证码是：" + code);
            mailSender.send(message);
            return code;
        } catch (MessagingException | MailException ex) {
            System.out.println(ex.getMessage());
            verificationCodes.remove(email);
            return null;
        }
    }


    public boolean verifyVerificationCode(String email, String inputCode) {
        String storedCode = verificationCodes.get(email);
        if (storedCode != null && storedCode.equals(inputCode)) {
            verificationCodes.remove(email);
            return true;
        }
        return false;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    
}