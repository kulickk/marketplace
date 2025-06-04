package com.project.marketplace.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        @Schema(description = "UUID заказа", example = "d4f3a2b6-0d7e-4f3a-ae1b-5f8c2b1a6c3d") UUID orderId,

        @Schema(description = "Идентификатор пользователя, оформившего заказ", example = "e1f2d3c4-...") UUID userId,

        @Schema(description = "Дата и время создания заказа") LocalDateTime createdAt,

        @Schema(description = "Общая сумма заказа", example = "1599.98") BigDecimal totalAmount,

        @Schema(description = "Текущий статус заказа", example = "NEW") String status,

        @Schema(description = "YooKassa paymentId (если уже создан) / null пока не создан", example = "00000000000001") String paymentId,

        @Schema(description = "Ссылка для подтверждения оплаты (если создан платёж)", example = "https://yookassa.ru/...") String paymentUrl,

        @Schema(description = "Статус самого платежа (pending, succeeded, canceled и т.п.)", example = "pending") String paymentStatus,

        @Schema(description = "Имя получателя", example = "Иван") String recipientFirstName,

        @Schema(description = "Фамилия получателя", example = "Петров") String recipientLastName,

        @Schema(description = "Телефон получателя", example = "+79123456789") String recipientPhone,

        @Schema(description = "Адрес пункта выдачи", example = "ул. Ленина, д. 10, ПВЗ №5") String pickupAddress,

        @Schema(description = "Список позиций заказа") List<OrderItemDto> items) {
}