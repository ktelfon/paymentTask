package com.payment.payment.service;

import com.payment.payment.config.EmailConfiguration;
import com.payment.payment.dto.PaymentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final EmailConfiguration configuration;
    private final JavaMailSender emailSender;

    public EmailService(EmailConfiguration configuration, JavaMailSender emailSender) {
        this.configuration = configuration;
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(PaymentDto payment, String emailTo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(configuration.getEmailFrom());
        message.setTo(emailTo);
        message.setSubject(configuration.getEmailSubject());
        message.setText("From: " + payment.from() + " To: " + payment.to() + " Amount: " + payment.amount());
        logger.info("Sending email notification to {}", emailTo);
        emailSender.send(message);

    }
}
