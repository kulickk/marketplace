package com.project.marketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@Data
public class MailConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private final Properties properties = new Properties();

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }
}