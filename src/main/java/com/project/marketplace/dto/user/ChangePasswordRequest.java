package com.project.marketplace.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank @Schema(description = "Текущий пароль") String currentPassword,
        @NotBlank @Size(min = 6, message = "Новый пароль минимум 6 символов") @Schema(description = "Новый пароль") String newPassword) {
}