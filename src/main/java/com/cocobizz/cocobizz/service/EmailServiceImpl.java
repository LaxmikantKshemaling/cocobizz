package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);
    }
}