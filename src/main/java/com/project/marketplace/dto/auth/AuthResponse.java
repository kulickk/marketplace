package com.project.marketplace.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ после успешного подтверждения OTP")
public record AuthResponse(
        @Schema(description = "Токен сессии", example = "session-token") String token,
        @Schema(description = "Сообщение", example = "Аутентификация прошла успешно") String message) {
}