package com.project.marketplace.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "yookassa")
public class YooKassaProperties {
    private String shopId;
    private String secretKey;
}