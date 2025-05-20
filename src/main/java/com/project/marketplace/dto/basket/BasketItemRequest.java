package com.project.marketplace.dto.basket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Запрос для добавления или обновления элемента корзины")
public record BasketItemRequest(
    @NotNull(message = "Идентификатор товара обязателен")
    @Schema(description = "Идентификатор товара", example = "42e9536b-b32e-44b5-9e3f-a960a9f3cc66")
    UUID goodId,
    
    @NotNull(message = "Количество товара не может быть пустым")
    @Min(value = 1, message = "Количество должно быть не менее 1")
    @Schema(description = "Количество товара", example = "2")
    Integer quantity
) {}
