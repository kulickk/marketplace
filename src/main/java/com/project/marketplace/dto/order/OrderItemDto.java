package com.project.marketplace.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;


public record OrderItemDto(
    @Schema(description = "Идентификатор позиции заказа", example = "b1f2ca3d-...") UUID id,
    @Schema(description = "Идентификатор товара", example = "81d5773d-...") UUID goodId,
    @Schema(description = "Название товара", example = "Смартфон Samsung") String name,
    @Schema(description = "Цена за штуку (на момент оформления)", example = "799.99") BigDecimal price,
    @Schema(description = "Количество единиц товара", example = "2") int quantity,
    @Schema(description = "Первая картинка товара (если есть)", example = "abcd1234.jpg") String imagePath
) {}