package com.payment.payment.controller;

import com.payment.payment.dto.NotificationDto;
import com.payment.payment.dto.PaymentDto;
import com.payment.payment.service.EmailService;
import jakarta.validation.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.payment.payment.controller.PaymentController.CONTROLLER_PATH;

@RestController
@RequestMapping(CONTROLLER_PATH)
@Validated
public class PaymentController {

    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    public static final String CONTROLLER_PATH = "/payment";
    public static final String EMAIL_NOTIFICATION_PATH = "/email/{emailTo}";
    private final EmailService emailService;

    public PaymentController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping(EMAIL_NOTIFICATION_PATH)
    public ResponseEntity<NotificationDto> sendEmailNotification(@PathVariable("emailTo") @Email String emailTo, @RequestBody PaymentDto payment) {
        logger.info("Received a request to send a email notification to {}", emailTo);
        emailService.sendSimpleMessage(payment, emailTo);
        return ResponseEntity.ok(new NotificationDto(buildEmailNotificationRespMessage(emailTo)));
    }

    public static String buildEmailNotificationRespMessage(String emailTo) {
        return "Email notification sent to " + emailTo;
    }
}
