package com.project.marketplace.controller;

import com.project.marketplace.dto.seller.SellerOrderDto;
import com.project.marketplace.model.Role;
import com.project.marketplace.service.SellerService;
import com.project.marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final UserService userService;

    private UUID getCurrentUserId(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
        }
        try {
            return UUID.fromString(session.getAttribute("userId").toString());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный формат userId в сессии");
        }
    }

    private void checkSeller(UUID userId) {
        var user = userService.getUserById(userId);
        if (user.getRole() != Role.SELLER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ разрешён только SELLER");
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<List<SellerOrderDto>> getMyOrders(HttpServletRequest request) {
        UUID me = getCurrentUserId(request);
        checkSeller(me);
        List<SellerOrderDto> orders = sellerService.listOrdersForSeller(me);
        return ResponseEntity.ok(orders);
    }
}