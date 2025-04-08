package com.project.marketplace.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на подтверждение OTP кода")
public record ConfirmOtpRequest(
    @NotBlank(message = "Временный идентификатор не должен быть пустым")
    @Schema(description = "Временный идентификатор, полученный после логина", example = "c9b1e1c8-...")
    String loginId,
    
    @NotBlank(message = "OTP код не должен быть пустым")
    @Schema(description = "Одноразовый код, полученный на почту", example = "123456")
    String otp
) {}