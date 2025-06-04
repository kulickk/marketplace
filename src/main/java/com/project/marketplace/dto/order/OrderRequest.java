package com.project.marketplace.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.UUID;


public record OrderRequest(
    @NotEmpty(message = "Нужно передать хотя бы один элемент корзины")
    @Schema(
        description = "Список UUID элементов корзины, которые пользователь выбирает для заказа",
        example = "[\"322b7de0-f334-4f15-adff-8635ef23b51d\", \"c0001bdb-9690-4057-92b2-f359d2e9d8f0\"]"
    )
    List<UUID> basketItemIds,

    @Schema(
        description = "Имя получателя. Если не указано, берётся из профиля пользователя",
        example = "Иван"
    )
    String recipientFirstName,

    @Schema(
        description = "Фамилия получателя. Если не указано, берётся из профиля пользователя",
        example = "Петров"
    )
    String recipientLastName,

    @Pattern(
        regexp = "^((\\+7)|8)\\d{10}$",
        message = "Номер телефона должен начинаться с +7 или 8 и содержать ровно 11 цифр"
    )
    @Schema(
        description = "Телефон получателя. Если не указано, берётся из профиля пользователя",
        example = "+79123456789"
    )
    String recipientPhone,

    @NotNull(message = "Адрес пункта выдачи обязателен")
    @Schema(
        description = "Адрес пункта выдачи, где клиент заберёт заказ",
        example = "ул. Ленина, д. 10, ПВЗ №5"
    )
    String pickupAddress
) {}