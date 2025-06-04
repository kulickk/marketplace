package com.project.marketplace.repository;

import com.project.marketplace.entity.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BasketItemRepository extends JpaRepository<BasketItem, UUID> {
    Optional<BasketItem> findByBasketIdAndGoodId(UUID basketId, UUID goodId);
    List<BasketItem> findByBasketId(UUID basketId);
}
