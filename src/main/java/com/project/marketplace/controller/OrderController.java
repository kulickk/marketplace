package com.project.marketplace.controller;

import com.project.marketplace.dto.order.OrderRequest;
import com.project.marketplace.dto.order.OrderResponse;
import com.project.marketplace.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private UUID extractUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
        }
        try {
            return UUID.fromString(session.getAttribute("userId").toString());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Неверный формат userId");
        }
    }

    @Operation(summary = "Создать заказ из выбранных элементов корзины")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            HttpServletRequest request,
            @Valid @RequestBody OrderRequest orderRequest) {

        UUID userId = extractUserId(request);
        OrderResponse response = orderService.createOrder(userId, orderRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить информацию о заказе по его ID")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            HttpServletRequest request,
            @PathVariable("orderId")
            @Parameter(description = "UUID заказа", example = "d4f3a2b6-0d7e-4f3a-ae1b-5f8c2b1a6c3d")
            UUID orderId) {

        UUID userId = extractUserId(request);
        OrderResponse response = orderService.getOrder(userId, orderId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить список всех заказов текущего пользователя")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(HttpServletRequest request) {
        UUID userId = extractUserId(request);
        List<OrderResponse> orders = orderService.getOrdersForUser(userId);
        return ResponseEntity.ok(orders);
    }
}