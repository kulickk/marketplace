package com.project.marketplace.controller;

import com.project.marketplace.dto.goodcategories.GoodCategoryRequest;
import com.project.marketplace.dto.goodcategories.GoodCategoryResponse;
import com.project.marketplace.service.AdminService;
import com.project.marketplace.service.GoodCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/good-categories")
@RequiredArgsConstructor
public class GoodCategoryController {

    private final GoodCategoryService goodCategoryService;
    private final AdminService adminService;

    private UUID getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
        }
        try {
            return UUID.fromString(session.getAttribute("userId").toString());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный формат userId в сессии");
        }
    }
    
    private void checkAdmin(HttpServletRequest request) {
        UUID userId = getCurrentUserId(request);
        adminService.checkIsAdmin(userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение категории по ID")
    public ResponseEntity<GoodCategoryResponse> getCategory(@PathVariable("id") UUID id) {
        GoodCategoryResponse response = goodCategoryService.getCategory(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Получение списка категорий")
    public ResponseEntity<List<GoodCategoryResponse>> getAllCategories() {
        List<GoodCategoryResponse> responses = goodCategoryService.getAllCategories();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @Operation(summary = "Создание категории")
    public ResponseEntity<GoodCategoryResponse> createCategory(
            HttpServletRequest request,
            @RequestBody @Valid GoodCategoryRequest requestBody) {
        checkAdmin(request);
        GoodCategoryResponse response = goodCategoryService.createCategory(requestBody);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление категории по ID")
    public ResponseEntity<GoodCategoryResponse> updateCategory(
            HttpServletRequest request,
            @PathVariable("id") UUID id,
            @RequestBody @Valid GoodCategoryRequest requestBody) {
        checkAdmin(request);
        GoodCategoryResponse response = goodCategoryService.updateCategory(id, requestBody);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление категории по ID")
    public ResponseEntity<Void> deleteCategory(
            HttpServletRequest request,
            @PathVariable("id") UUID id) {
        checkAdmin(request);            
        goodCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
