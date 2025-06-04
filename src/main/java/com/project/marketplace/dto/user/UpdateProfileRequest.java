package com.project.marketplace.dto.user;

import com.project.marketplace.model.Gender;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateProfileRequest(
    @NotBlank @Schema(description = "Имя") String firstName,
    @NotBlank @Schema(description = "Фамилия") String lastName,
    @Schema(description = "Пол", example = "MALE") Gender gender,
    @Pattern(
        regexp = "^((\\+7)|8)\\d{10}$",
        message = "Номер телефона должен начинаться с +7 или 8 и содержать ровно 11 цифр (без пробелов, скобок и дефисов)"
    )
    @Schema(description = "Номер телефона", example = "+79123456789")
    String phoneNumber
) {}