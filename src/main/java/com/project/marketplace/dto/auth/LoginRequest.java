package com.project.marketplace.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Данные для авторизации пользователя")
public record LoginRequest(
    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    @Schema(description = "Email пользователя", example = "user@example.com")
    String email,

    @NotBlank(message = "Пароль не должен быть пустым")
    @Schema(description = "Пароль", example = "Qwerty123!")
    String password
) {}