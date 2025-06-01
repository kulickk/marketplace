package com.project.marketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Разрешаем абсолютно все источники (Work in tandem with allowCredentials)
        config.addAllowedOriginPattern("*");

        // Разрешаем отправлять любые заголовки
        config.addAllowedHeader("*");

        // Разрешаем любые HTTP-методы (GET, POST, PUT, DELETE, OPTIONS, и т.д.)
        config.addAllowedMethod("*");

        // Разрешаем браузеру передавать Cookie/креденшалы (например, HTTP-only SESSION)
        config.setAllowCredentials(true);

        // (Опционально) какие заголовки клиент может читать из ответа
        config.addExposedHeader("Set-Cookie");
        config.addExposedHeader("Authorization");

        // Привязываем данную конфигурацию ко всем URL приложения
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}