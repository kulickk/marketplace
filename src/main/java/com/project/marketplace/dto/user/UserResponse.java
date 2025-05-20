package com.project.marketplace.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Профиль пользователя")
public record UserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}