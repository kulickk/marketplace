package com.project.marketplace.controller;

import com.project.marketplace.dto.goods.GoodRequest;
import com.project.marketplace.dto.goods.GoodResponse;
import com.project.marketplace.entity.Good;
import com.project.marketplace.entity.GoodImage;
import com.project.marketplace.entity.User;
import com.project.marketplace.model.Role;
import com.project.marketplace.service.FileStorageService;
import com.project.marketplace.service.GoodsService;
import com.project.marketplace.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsService goodsService;
    private final UserService userService;
    private final FileStorageService fileStorageService;

    private UUID getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
        }
        Object raw = session.getAttribute("userId");
        if (raw == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
        }
        try {
            return UUID.fromString(raw.toString());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный формат userId в сессии");
        }
    }

    private void checkSeller(HttpServletRequest request) {
        UUID userId = getCurrentUserId(request);
        User user = userService.getUserById(userId);
        if (user.getRole() != Role.SELLER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Доступ запрещён: требуется роль SELLER");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение товара по ID")
    public ResponseEntity<GoodResponse> getGoodById(@PathVariable("id") UUID id) {
        GoodResponse response = goodsService.getGoodById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Получение списка всех товаров (с пагинацией)")
    public ResponseEntity<List<GoodResponse>> getAllGoods(
            @RequestParam(name = "page", defaultValue = "0") @Parameter(description = "Номер страницы (0-индекс)", example = "0") int page,

            @RequestParam(name = "size", defaultValue = "10") @Parameter(description = "Размер страницы", example = "10") int size) {
        List<GoodResponse> responses = goodsService.getAllGoods(page, size);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Получение товаров по категории (с пагинацией)")
    public ResponseEntity<List<GoodResponse>> getGoodsByCategory(
            @PathVariable("categoryId") @Parameter(description = "UUID категории", example = "d4f3a2b6-0d7e-4f3a-ae1b-5f8c2b1a6c3d") UUID categoryId,

            @RequestParam(name = "page", defaultValue = "0") @Parameter(description = "Номер страницы (0-индекс)", example = "0") int page,

            @RequestParam(name = "size", defaultValue = "10") @Parameter(description = "Размер страницы", example = "10") int size) {
        List<GoodResponse> responses = goodsService.getGoodsByCategory(categoryId, page, size);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @Operation(summary = "Создание товара (только SELLER)")
    public ResponseEntity<GoodResponse> createGood(
            HttpServletRequest request,
            @RequestBody @Valid GoodRequest requestBody) {

        checkSeller(request);
        UUID ownerId = getCurrentUserId(request);
        GoodResponse response = goodsService.createGood(ownerId, requestBody);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление товара по ID (только SELLER && владелец)")
    public ResponseEntity<GoodResponse> updateGood(
            HttpServletRequest request,
            @PathVariable("id") UUID id,
            @RequestBody @Valid GoodRequest requestBody) {

        checkSeller(request);
        UUID ownerId = getCurrentUserId(request);

        GoodResponse response = goodsService.updateGood(id, ownerId, requestBody);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление товара по ID (только SELLER && владелец)")
    public ResponseEntity<Void> deleteGood(
            HttpServletRequest request,
            @PathVariable("id") UUID id) {

        checkSeller(request);
        UUID ownerId = getCurrentUserId(request);

        goodsService.deleteGood(id, ownerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск товаров по имени (с пагинацией)")
    public ResponseEntity<List<GoodResponse>> searchGoods(
            @RequestParam("query") @Parameter(description = "Подстрока для поиска в названии", example = "смартфон") String query,

            @RequestParam(name = "page", defaultValue = "0") @Parameter(description = "Номер страницы (0-индекс)", example = "0") int page,

            @RequestParam(name = "size", defaultValue = "10") @Parameter(description = "Размер страницы", example = "10") int size) {
        if (query == null || query.isBlank()) {
            List<GoodResponse> all = goodsService.getAllGoods(page, size);
            return ResponseEntity.ok(all);
        }
        List<GoodResponse> results = goodsService.searchGoodsByName(query.trim(), page, size);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/get-my-goods")
    @Operation(summary = "Получить свои товары (только SELLER, с пагинацией)")
    public ResponseEntity<List<GoodResponse>> getMyGoods(
            HttpServletRequest request,

            @RequestParam(name = "page", defaultValue = "0") @Parameter(description = "Номер страницы (0-индекс)", example = "0") int page,

            @RequestParam(name = "size", defaultValue = "10") @Parameter(description = "Размер страницы", example = "10") int size) {
        checkSeller(request);
        UUID ownerId = getCurrentUserId(request);
        List<GoodResponse> responses = goodsService.getMyGoods(ownerId, page, size);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Загрузить картинку для товара (до 4 штук, только SELLER-владелец)", description = "Принимает один файл image/*, если у товара ещё меньше 4 изображений", responses = {
            @ApiResponse(responseCode = "204", description = "Изображение загружено"),
            @ApiResponse(responseCode = "400", description = "Максимум 4 изображения или неверный тип файла"),
            @ApiResponse(responseCode = "401", description = "Неавторизован"),
            @ApiResponse(responseCode = "403", description = "Нет прав (не SELLER или не владелец товара)")
    })
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadGoodImage(
            HttpServletRequest request,

            @PathVariable("id") @Parameter(description = "UUID товара", example = "550e8400-e29b-41d4-a716-446655440000") UUID goodId,

            @RequestPart("file") @Parameter(description = "Файл-картинка (image/*)", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(type = "string", format = "binary"))) MultipartFile file) {
        checkSeller(request);
        UUID ownerId = getCurrentUserId(request);

        Good good = goodsService.getGoodEntityById(goodId);
        if (good.getOwner() == null || !good.getOwner().getId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет доступа: этот товар не ваш");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нужно загрузить файл-изображение");
        }

        int existingCount = goodsService.getGoodEntityById(goodId).getImages().size();
        if (existingCount >= 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нельзя загрузить более 4 изображений на товар");
        }

        String newFilename = fileStorageService.storeFile(file);

        GoodImage goodImage = GoodImage.builder()
                .good(good)
                .imagePath(newFilename)
                .build();
        good.getImages().add(goodImage);
        goodsService.saveGoodEntity(good);

        return ResponseEntity.noContent().build();
    }
}
