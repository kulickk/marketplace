package com.project.marketplace.dto.seller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SellerOrderDto(
    UUID orderId,
    LocalDateTime createdAt,
    BigDecimal totalAmount,
    String status,
    UUID userId,
    String userFirstName,
    String userLastName,
    List<SellerOrderItemDto> items
) {}