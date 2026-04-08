package com.lastcalleats.payment.config;

import com.stripe.Stripe;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.stripe")
public class StripeConfig {

    private String secretKey;
    private String webhookSecret;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }
}
