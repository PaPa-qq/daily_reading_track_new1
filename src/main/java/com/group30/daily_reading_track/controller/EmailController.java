package com.group30.daily_reading_track.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group30.daily_reading_track.service.EmailService;

import jakarta.mail.MessagingException;

    @RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/sendEmail")
    public void sendMail(){
        emailService.sendEmail();
    }
}

