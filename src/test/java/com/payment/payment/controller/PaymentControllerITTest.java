package com.payment.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.payment.dto.PaymentDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.payment.payment.controller.PaymentController.buildEmailNotificationRespMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JavaMailSender sender;

    @Test
    void happyPathForSendingEmailNotification() throws Exception {
        String path = PaymentController.CONTROLLER_PATH + PaymentController.EMAIL_NOTIFICATION_PATH;
        String email = "test@test.com";

        PaymentDto paymentDto = new PaymentDto("me@go.com", "you@go.com", 900.0);
        mockMvc.perform(MockMvcRequestBuilders.post(path, email)
                        .content(new ObjectMapper().writeValueAsString(paymentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg", Matchers.equalTo(buildEmailNotificationRespMessage(email))))
                .andDo(MockMvcResultHandlers.print());

        verify(sender, times(1)).send(any(SimpleMailMessage.class));

        final ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(sender).send(captor.capture());
        final SimpleMailMessage argument = captor.getValue();
        assertEquals(email, argument.getTo()[0]);

    }

    @Test
    void errorOnSendingEmail() throws Exception {
        String path = PaymentController.CONTROLLER_PATH + PaymentController.EMAIL_NOTIFICATION_PATH;
        String email = "test@test.com";

        PaymentDto paymentDto = new PaymentDto("me@go.com", "you@go.com", 900.0);
        doThrow(new MailSendException("Error occurred"))
                .when(sender)
                .send(any(SimpleMailMessage.class));
        mockMvc.perform(MockMvcRequestBuilders.post(path, email)
                        .content(new ObjectMapper().writeValueAsString(paymentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.text", Matchers.equalTo(PaymentControllerAdvice.PAYMENT_ERROR_TEXT)))
                .andDo(MockMvcResultHandlers.print());

    }
}