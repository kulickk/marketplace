package com.project.marketplace.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Данные для регистрации пользователя")
public record RegisterRequest(
    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    @Schema(description = "Email пользователя", example = "user@example.com")
    String email,

    @NotBlank(message = "Пароль не должен быть пустым")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    @Schema(description = "Пароль", example = "Qwerty123!")
    String password,

    @NotBlank(message = "Имя не должно быть пустым")
    @Schema(description = "Имя", example = "Иван")
    String firstName,

    @NotBlank(message = "Фамилия не должна быть пустой")
    @Schema(description = "Фамилия", example = "Петров")
    String lastName) {
}
