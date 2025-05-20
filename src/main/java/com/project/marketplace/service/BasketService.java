package com.project.marketplace.service;

import com.project.marketplace.dto.basket.BasketItemRequest;
import com.project.marketplace.dto.basket.BasketItemResponse;
import com.project.marketplace.dto.basket.BasketResponse;
import com.project.marketplace.entity.Basket;
import com.project.marketplace.entity.BasketItem;
import com.project.marketplace.repository.BasketItemRepository;
import com.project.marketplace.repository.BasketRepository;
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

    /**
     * Получает корзину пользователя по userId. Если корзина не существует, создаёт новую.
     */
    @Transactional
    public BasketResponse getBasket(UUID userId) {
        Basket basket = basketRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Basket newBasket = Basket.builder().userId(userId).build();
                    return basketRepository.save(newBasket);
                });
        return mapToBasketResponse(basket);
    }

    /**
     * Добавляет новый элемент в корзину или обновляет количество, если такой элемент уже существует.
     */
    @Transactional
    public BasketItemResponse addOrUpdateBasketItem(UUID userId, BasketItemRequest request) {
        Basket basket = basketRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Basket newBasket = Basket.builder().userId(userId).build();
                    return basketRepository.save(newBasket);
                });

        // Ищем элемент с заданным товаром в корзине
        BasketItem basketItem = basketItemRepository.findByBasketIdAndGoodId(basket.getId(), request.goodId())
                .orElse(null);
        if (basketItem != null) {
            // Обновляем количество
            basketItem.setQuantity(request.quantity());
        } else {
            // Создаем новый элемент
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

    /**
     * Обновляет количество товара у элемента корзины по его ID.
     */
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

    /**
     * Удаляет элемент корзины по его ID.
     */
    @Transactional
    public void deleteBasketItem(UUID userId, UUID basketItemId) {
        BasketItem basketItem = basketItemRepository.findById(basketItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Элемент корзины не найден"));
        if (!basketItem.getBasket().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет доступа для удаления этого элемента");
        }
        basketItemRepository.delete(basketItem);
    }

    private BasketItemResponse mapToBasketItemResponse(BasketItem item) {
        return new BasketItemResponse(item.getId(), item.getGoodId(), item.getQuantity());
    }

    private BasketResponse mapToBasketResponse(Basket basket) {
        List<BasketItemResponse> items = basket.getItems().stream()
                .map(this::mapToBasketItemResponse)
                .collect(Collectors.toList());
        return new BasketResponse(basket.getId(), items);
    }
}
