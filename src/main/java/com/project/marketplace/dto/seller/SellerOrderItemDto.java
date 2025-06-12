package com.project.marketplace.dto.seller;

import java.math.BigDecimal;
import java.util.UUID;

public record SellerOrderItemDto(
    UUID goodId,
    String name,
    int quantity,
    BigDecimal price
) {}