package com.project.marketplace.config;

import com.project.marketplace.interceptor.RedisSessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StringRedisTemplate redisTemplate;

    public WebConfig(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new RedisSessionInterceptor(redisTemplate))
                .addPathPatterns("/api/users/**")
                .excludePathPatterns("/api/v1/auth/**");
    }

}
