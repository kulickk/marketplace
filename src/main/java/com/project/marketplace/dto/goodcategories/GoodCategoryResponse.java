package com.project.marketplace.dto.goodcategories;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

@Schema(description = "Ответ для категории товаров")
public record GoodCategoryResponse(
    UUID id,
    String name,
    String description,
    UUID parentId,
    List<GoodCategoryResponse> children
) {}
