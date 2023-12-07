package com.payment.payment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:email.yml")
public class EmailConfiguration {
    private String emailFrom;
    private String emailSubject;
    private String emailText;

    public String getEmailFrom() {
        return emailFrom;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public String getEmailText() {
        return emailText;
    }

}
