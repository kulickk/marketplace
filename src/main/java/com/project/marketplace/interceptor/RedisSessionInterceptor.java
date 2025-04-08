package com.project.marketplace.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;

public class RedisSessionInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate redisTemplate;

    public RedisSessionInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Пользователь не авторизован: cookies отсутствуют");
        }

        String sessionId = null;
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getValue());
            if ("SESSION".equals(cookie.getName())) {
                sessionId = cookie.getValue();
                break;
            }
        }

        if (sessionId == null || sessionId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Пользователь не авторизован: сессионный токен отсутствует");
        }

        byte[] decodedBytes = Base64.getDecoder().decode(sessionId);
        String decodedSessionId = new String(decodedBytes, StandardCharsets.UTF_8);

        String redisKey = "spring:session:sessions:" + decodedSessionId;
        Boolean exists = redisTemplate.hasKey(redisKey);
        if (exists == null || !exists) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Пользователь не авторизован: сессия не найдена в Redis");
        }

        return true;
    }
}
