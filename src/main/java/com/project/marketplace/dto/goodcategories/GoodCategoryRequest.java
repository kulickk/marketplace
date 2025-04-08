package com.project.marketplace.dto.goodcategories;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

@Schema(description = "Запрос на создание или обновление категории товаров")
public record GoodCategoryRequest(
    @NotBlank(message = "Название категории не должно быть пустым")
    @Schema(description = "Название категории", example = "Электроника")
    String name,

    @Schema(description = "Описание категории", example = "Вся электроника")
    String description,

    @Schema(description = "Идентификатор родительской категории", example = "d4f3a2b6-0d7e-4f3a-ae1b-5f8c2b1a6c3d")
    UUID parentId
) {}
