package com.project.marketplace.repository;

import com.project.marketplace.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
     List<OrderItem> findByOrderId(UUID orderId);

     List<OrderItem> findByGoodIdIn(List<UUID> goodIds);
}