package com.project.marketplace.dto.admin;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdminUserDto(
    UUID id,
    String firstName,
    String lastName,
    String email,
    String gender,
    String role,
    String phoneNumber,
    LocalDateTime createdAt
) {}