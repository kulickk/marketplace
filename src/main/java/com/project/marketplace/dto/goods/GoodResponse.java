package com.project.marketplace.dto.goods;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Schema(description = "Ответ с данными товара")
public record GoodResponse(
    UUID id,
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    String brand,
    UUID categoryId,
    UUID ownerId,
    List<String> imagePaths
) {}
