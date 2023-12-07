package com.payment.payment.controller;

import com.payment.payment.dto.PaymentError;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class PaymentControllerAdvice {

    public static final String PAYMENT_ERROR_TEXT = "Failed to send the notification.";

    @ExceptionHandler({MailException.class})
    public final ResponseEntity<PaymentError> handleException(Exception ex, WebRequest request) {
        return ResponseEntity.badRequest().body(new PaymentError(PAYMENT_ERROR_TEXT));
    }
}
