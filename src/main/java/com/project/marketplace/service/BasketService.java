package com.project.marketplace.service;

import com.project.marketplace.dto.basket.BasketItemRequest;
import com.project.marketplace.dto.basket.BasketItemResponse;
import com.project.marketplace.dto.basket.BasketResponse;
import com.project.marketplace.entity.Basket;
import com.project.marketplace.entity.BasketItem;
import com.project.marketplace.entity.Good;
import com.project.marketplace.entity.GoodImage;
import com.project.marketplace.repository.BasketItemRepository;
import com.project.marketplace.repository.BasketRepository;
import com.project.marketplace.repository.GoodImageRepository;
import com.project.marketplace.repository.GoodsRepository;

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
public class BasketService {

    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final GoodsRepository goodsRepository;
    private final GoodImageRepository goodImageRepository;

    @Transactional
    public BasketResponse getBasket(UUID userId) {
        Basket basket = basketRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Basket newBasket = Basket.builder().userId(userId).build();
                    return basketRepository.save(newBasket);
                });

        List<BasketItemResponse> items = basketItemRepository.findByBasketId(basket.getId()).stream()
                .map(this::mapToBasketItemResponse)
                .collect(Collectors.toList());

        return new BasketResponse(basket.getId(), items);
    }

    @Transactional
    public BasketItemResponse addOrUpdateBasketItem(UUID userId, BasketItemRequest request) {
        Basket basket = basketRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Basket newBasket = Basket.builder().userId(userId).build();
                    return basketRepository.save(newBasket);
                });

        BasketItem basketItem = basketItemRepository.findByBasketIdAndGoodId(basket.getId(), request.goodId())
                .orElse(null);
        if (basketItem != null) {
            basketItem.setQuantity(request.quantity());
        } else {
            basketItem = BasketItem.builder()
                    .basket(basket)
                    .goodId(request.goodId())
                    .quantity(request.quantity())
                    .build();
            basket.getItems().add(basketItem);
        }
        BasketItem savedItem = basketItemRepository.save(basketItem);
        return mapToBasketItemResponse(savedItem);
    }

    @Transactional
    public BasketItemResponse updateBasketItem(UUID userId, UUID basketItemId, BasketItemRequest request) {
        BasketItem basketItem = basketItemRepository.findById(basketItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Элемент корзины не найден"));
        if (!basketItem.getBasket().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет доступа для изменения этого элемента");
        }
        basketItem.setQuantity(request.quantity());
        BasketItem savedItem = basketItemRepository.save(basketItem);
        return mapToBasketItemResponse(savedItem);
    }

    @Transactional
    public void deleteBasketItem(UUID userId, UUID basketItemId) {
        BasketItem basketItem = basketItemRepository.findById(basketItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Элемент корзины не найден"));
        if (!basketItem.getBasket().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет доступа для удаления этого элемента");
        }
        basketItemRepository.delete(basketItem);
    }

    @Transactional
    public void clearBasket(UUID userId) {
        Basket basket = basketRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Корзина не найдена"));
        List<BasketItem> items = basketItemRepository.findByBasketId(basket.getId());
        basketItemRepository.deleteAll(items);
    }

    private BasketItemResponse mapToBasketItemResponse(BasketItem item) {
        UUID goodId = item.getGoodId();

        Good good = goodsRepository.findById(goodId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Товар с id " + goodId + " не найден"));

        String name = good.getName();
        java.math.BigDecimal price = good.getPrice();

        List<GoodImage> images = goodImageRepository.findByGoodId(goodId);
        String firstImagePath = images.isEmpty() ? null : images.get(0).getImagePath();

        return new BasketItemResponse(
                item.getId(),
                goodId,
                item.getQuantity(),
                name,
                price,
                firstImagePath);
    }
}