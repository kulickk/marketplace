package com.project.marketplace.service;

import com.project.marketplace.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final StringRedisTemplate redisTemplate;

    public String initiateLogin(String email, String password) {
        User user = userService.getUserByEmail(email);
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный email или пароль");
        }

        String lockKey = "otp_lock:" + user.getEmail();
        Boolean lockExists = redisTemplate.hasKey(lockKey);
        if (Boolean.TRUE.equals(lockExists)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "OTP код уже отправлен. Повторите попытку через минуту");
        }

        String otpCode = String.valueOf((int) ((Math.random() * 900000) + 100000));
        String loginId = UUID.randomUUID().toString();
        String redisKey = "login:" + loginId;

        redisTemplate.opsForHash().put(redisKey, "otp", otpCode);
        redisTemplate.opsForHash().put(redisKey, "userId", user.getId().toString());
        redisTemplate.expire(redisKey, Duration.ofMinutes(5));

        redisTemplate.opsForValue().set(lockKey, "1", Duration.ofMinutes(1));

        emailService.sendOtpEmail(user.getEmail(), otpCode);
        return loginId;
    }

    public String confirmOtp(String loginId, String otp, HttpServletRequest request) {
        String redisKey = "login:" + loginId;
        Map<Object, Object> loginData = redisTemplate.opsForHash().entries(redisKey);
        if (loginData == null || loginData.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный или просроченный идентификатор");
        }

        String savedOtp = (String) loginData.get("otp");
        String savedUserId = (String) loginData.get("userId");

        if (savedOtp == null || !savedOtp.equals(otp)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный OTP код");
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("userId", savedUserId);
        redisTemplate.delete(redisKey);
        return session.getId();
    }
}
