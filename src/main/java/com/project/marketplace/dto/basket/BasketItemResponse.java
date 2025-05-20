package com.project.marketplace.dto.basket;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Ответ с данными элемента корзины")
public record BasketItemResponse(
    UUID id,
    UUID goodId,
    Integer quantity
) {}
