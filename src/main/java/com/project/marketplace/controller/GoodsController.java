package com.project.marketplace.controller;

import com.project.marketplace.dto.goods.GoodRequest;
import com.project.marketplace.dto.goods.GoodResponse;
import com.project.marketplace.service.GoodsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsService goodsService;

    @GetMapping("/{id}")
    @Operation(summary = "Получение товара по ID")
    public ResponseEntity<GoodResponse> getGoodById(@PathVariable("id") UUID id) {
        GoodResponse response = goodsService.getGoodById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Получение списка товаров")
    public ResponseEntity<List<GoodResponse>> getAllGoods() {
        List<GoodResponse> responses = goodsService.getAllGoods();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @Operation(summary = "Создание товара")
    public ResponseEntity<GoodResponse> createGood(@RequestBody @Valid GoodRequest request) {
        GoodResponse response = goodsService.createGood(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление товара по ID")
    public ResponseEntity<GoodResponse> updateGood(@PathVariable("id") UUID id,
                                                   @RequestBody @Valid GoodRequest request) {
        GoodResponse response = goodsService.updateGood(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление товара по ID")
    public ResponseEntity<Void> deleteGood(@PathVariable("id") UUID id) {
        goodsService.deleteGood(id);
        return ResponseEntity.noContent().build();
    }
}
