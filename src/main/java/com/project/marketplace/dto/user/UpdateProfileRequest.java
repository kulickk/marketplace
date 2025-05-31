package com.project.marketplace.dto.user;

import com.project.marketplace.model.Gender;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
    @NotBlank @Schema(description = "Имя") String firstName,
    @NotBlank @Schema(description = "Фамилия") String lastName,
    @Schema(description = "Пол", example = "MALE") Gender gender
) {}