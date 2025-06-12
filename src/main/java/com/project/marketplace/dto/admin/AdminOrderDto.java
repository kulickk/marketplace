package com.project.marketplace.dto.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AdminOrderDto(
    UUID orderId,
    UUID userId,
    String userFirstName,
    String userLastName,
    LocalDateTime createdAt,
    BigDecimal totalAmount,
    String status,
    List<AdminOrderItemDto> items
) {}