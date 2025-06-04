package com.project.marketplace.controller;

import com.project.marketplace.dto.basket.BasketItemRequest;
import com.project.marketplace.dto.basket.BasketItemResponse;
import com.project.marketplace.dto.basket.BasketResponse;
import com.project.marketplace.service.BasketService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/me/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @GetMapping
    @Operation(summary = "Получение корзины текущего пользователя")
    public ResponseEntity<BasketResponse> getBasket(HttpServletRequest request) {
        UUID userId = extractUserIdFromSession(request);
        BasketResponse basket = basketService.getBasket(userId);
        return ResponseEntity.ok(basket);
    }

    @PostMapping("/items")
    @Operation(summary = "Добавление или обновление элемента корзины")
    public ResponseEntity<BasketItemResponse> addOrUpdateItem(HttpServletRequest request,
            @RequestBody @Valid BasketItemRequest requestItem) {
        UUID userId = extractUserIdFromSession(request);
        BasketItemResponse item = basketService.addOrUpdateBasketItem(userId, requestItem);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PatchMapping("/items/{basketItemId}")
    @Operation(summary = "Обновление количества товара в элементе корзины")
    public ResponseEntity<BasketItemResponse> updateItem(HttpServletRequest request,
            @PathVariable("basketItemId") UUID basketItemId,
            @RequestBody @Valid BasketItemRequest requestItem) {
        UUID userId = extractUserIdFromSession(request);
        BasketItemResponse item = basketService.updateBasketItem(userId, basketItemId, requestItem);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/items/{basketItemId}")
    @Operation(summary = "Удаление элемента корзины")
    public ResponseEntity<Void> deleteItem(HttpServletRequest request,
            @PathVariable("basketItemId") UUID basketItemId) {
        UUID userId = extractUserIdFromSession(request);
        basketService.deleteBasketItem(userId, basketItemId);
        return ResponseEntity.noContent().build();
    }

    public static UUID extractUserIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
        }

        String userId = session.getAttribute("userId").toString();
        try {
            UUID uuid = UUID.fromString(userId);
            return uuid;
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException();
        }
    }

    @DeleteMapping("/items")
    public ResponseEntity<Void> clearBasket(HttpServletRequest request) {
        UUID userId = extractUserIdFromSession(request);
        basketService.clearBasket(userId);
        return ResponseEntity.noContent().build();
    }

}
