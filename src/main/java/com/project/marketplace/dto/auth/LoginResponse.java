package com.project.marketplace.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ на запрос логина. OTP отправлен на почту")
public record LoginResponse(
    @Schema(description = "Временный идентификатор для подтверждения OTP", example = "c9b1e1c8-...")
    String loginId,
    @Schema(description = "Сообщение", example = "OTP отправлен на почту")
    String message
) {}