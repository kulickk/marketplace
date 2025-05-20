package com.project.marketplace.repository;

import com.project.marketplace.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface BasketRepository extends JpaRepository<Basket, UUID> {
    Optional<Basket> findByUserId(UUID userId);
}