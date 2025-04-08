package com.project.marketplace.dto.goods;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Запрос на создание или обновление товара")
public record GoodRequest(
    @NotBlank(message = "Название товара не должно быть пустым")
    @Schema(description = "Название товара", example = "Смартфон")
    String name,

    @Schema(description = "Описание товара", example = "Последняя модель смартфона с высокими характеристиками")
    String description,

    @NotNull(message = "Цена товара обязательна")
    @Schema(description = "Цена товара", example = "999.99")
    BigDecimal price,

    @NotNull(message = "Количество товара не может быть пустым")
    @Schema(description = "Количество товара на складе", example = "10")
    Integer stock,

    @Schema(description = "Бренд товара", example = "Samsung")
    String brand,

    @NotNull(message = "Идентификатор категории обязателен")
    @Schema(description = "Идентификатор категории, в которой находится товар", example = "d4f3a2b6-0d7e-4f3a-ae1b-5f8c2b1a6c3d")
    UUID categoryId
) {}
