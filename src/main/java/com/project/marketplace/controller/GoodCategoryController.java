package com.project.marketplace.controller;

import com.project.marketplace.dto.goodcategories.GoodCategoryRequest;
import com.project.marketplace.dto.goodcategories.GoodCategoryResponse;
import com.project.marketplace.service.GoodCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/good-categories")
@RequiredArgsConstructor
public class GoodCategoryController {

    private final GoodCategoryService goodCategoryService;

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
    public ResponseEntity<GoodCategoryResponse> createCategory(@RequestBody @Valid GoodCategoryRequest request) {
        GoodCategoryResponse response = goodCategoryService.createCategory(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление категории по ID")
    public ResponseEntity<GoodCategoryResponse> updateCategory(@PathVariable("id") UUID id,
                                                               @RequestBody @Valid GoodCategoryRequest request) {
        GoodCategoryResponse response = goodCategoryService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление категории по ID")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") UUID id) {
        goodCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
