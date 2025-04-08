package com.project.marketplace.service;

import com.project.marketplace.dto.goods.GoodRequest;
import com.project.marketplace.dto.goods.GoodResponse;
import com.project.marketplace.entity.Good;
import com.project.marketplace.entity.GoodCategory;
import com.project.marketplace.repository.GoodsRepository;
import com.project.marketplace.repository.GoodCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsRepository goodsRepository;
    private final GoodCategoryRepository goodCategoryRepository; 

    @Transactional(readOnly = true)
    public GoodResponse getGoodById(UUID id) {
        Good good = goodsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));
        return mapToResponse(good);
    }

    @Transactional(readOnly = true)
    public List<GoodResponse> getAllGoods() {
        return goodsRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public GoodResponse createGood(GoodRequest request) {
        GoodCategory category = goodCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Категория не найдена"));

        Good good = Good.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .stock(request.stock())
                .brand(request.brand())
                .category(category)
                .build();
        Good saved = goodsRepository.save(good);
        return mapToResponse(saved);
    }

    @Transactional
    public GoodResponse updateGood(UUID id, GoodRequest request) {
        Good good = goodsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));

        GoodCategory category = goodCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Категория не найдена"));

        good.setName(request.name());
        good.setDescription(request.description());
        good.setPrice(request.price());
        good.setStock(request.stock());
        good.setBrand(request.brand());
        good.setCategory(category);

        Good saved = goodsRepository.save(good);
        return mapToResponse(saved);
    }

    @Transactional
    public void deleteGood(UUID id) {
        Good good = goodsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));
        goodsRepository.delete(good);
    }

    private GoodResponse mapToResponse(Good good) {
        UUID categoryId = good.getCategory() != null ? good.getCategory().getId() : null;
        return new GoodResponse(
                good.getId(),
                good.getName(),
                good.getDescription(),
                good.getPrice(),
                good.getStock(),
                good.getBrand(),
                categoryId
        );
    }
}
