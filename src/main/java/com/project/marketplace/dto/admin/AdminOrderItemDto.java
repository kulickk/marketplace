package com.project.marketplace.dto.admin;

import java.math.BigDecimal;
import java.util.UUID;

public record AdminOrderItemDto(
    UUID goodId,
    String name,
    int quantity,
    BigDecimal price
) {}