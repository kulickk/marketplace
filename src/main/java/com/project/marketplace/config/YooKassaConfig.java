package com.project.marketplace.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class YooKassaConfig {

    @Bean
    public WebClient yookassaWebClient(YooKassaProperties props) {
        String basicAuth = props.getShopId() + ":" + props.getSecretKey();
        String encodedAuth = java.util.Base64.getEncoder()
            .encodeToString(basicAuth.getBytes());

        return WebClient.builder()
                .baseUrl("https://api.yookassa.ru/v3")
                .defaultHeader("Authorization", "Basic " + encodedAuth)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}