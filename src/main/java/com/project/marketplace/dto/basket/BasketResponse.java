package com.project.marketplace.dto.basket;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

@Schema(description = "Ответ с данными корзины пользователя")
public record BasketResponse(
    UUID basketId,
    List<BasketItemResponse> items
) {}
