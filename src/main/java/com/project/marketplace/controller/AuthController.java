package com.project.marketplace.controller;

import com.project.marketplace.dto.auth.AuthResponse;
import com.project.marketplace.dto.auth.ConfirmOtpRequest;
import com.project.marketplace.dto.auth.LoginRequest;
import com.project.marketplace.dto.auth.LoginResponse;
import com.project.marketplace.dto.auth.RegisterRequest;
import com.project.marketplace.dto.auth.UserResponse;
import com.project.marketplace.service.AuthService;
import com.project.marketplace.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Отправка OTP на почту")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        String loginId = authService.initiateLogin(request.email(), request.password());
        return ResponseEntity.ok(new LoginResponse(loginId, "OTP отправлен на почту"));
    }

    @PostMapping("/logout")
    @Operation(summary = "Выход из системы (invalidate session)")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.initiateLogout(request, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/confirm")
    @Operation(summary = "Подтверждение OTP и выдача токена сессии")
    public ResponseEntity<AuthResponse> confirmOtp(
            @RequestBody @Valid ConfirmOtpRequest confirmRequest,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        String sessionId = authService.confirmOtp(
                confirmRequest.loginId(),
                confirmRequest.otp(),
                httpRequest);
        return ResponseEntity.ok(
                new AuthResponse(sessionId, "Аутентификация прошла успешно"));
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterRequest request) {
        UserResponse user = userService.createUser(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/some-endpoint")
    public ResponseEntity<String> someEndpoint(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
        }

        String userId = session.getAttribute("userId").toString();
        return ResponseEntity.ok("Сессия валидна для пользователя: " + userId);
    }

}
