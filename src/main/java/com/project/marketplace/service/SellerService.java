package com.project.marketplace.service;

import com.project.marketplace.dto.seller.SellerOrderDto;
import com.project.marketplace.dto.seller.SellerOrderItemDto;
import com.project.marketplace.entity.Good;
import com.project.marketplace.entity.Order;
import com.project.marketplace.entity.OrderItem;
import com.project.marketplace.entity.User;
import com.project.marketplace.repository.GoodsRepository;
import com.project.marketplace.repository.OrderItemRepository;
import com.project.marketplace.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final GoodsRepository goodsRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final GoodsService goodsService;
    private final UserService userService;

    @Transactional
    public List<SellerOrderDto> listOrdersForSeller(UUID sellerId) {
        List<UUID> myGoodIds = goodsRepository.findByOwnerId(sellerId)
                .stream()
                .map(Good::getId)
                .toList();
        if (myGoodIds.isEmpty()) {
            return List.of();
        }

        List<OrderItem> myItems = orderItemRepository.findByGoodIdIn(myGoodIds);

        Map<UUID, List<OrderItem>> byOrder = myItems.stream()
                .collect(Collectors.groupingBy(item -> item.getOrder().getId()));

        return byOrder.entrySet().stream().map(entry -> {
            UUID orderId = entry.getKey();
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalStateException("Заказ не найден: " + orderId));

            List<SellerOrderItemDto> items = entry.getValue().stream()
                    .map(item -> {
                        Good good = goodsService.getGoodEntityById(item.getGoodId());
                        return new SellerOrderItemDto(
                                good.getId(),
                                good.getName(),
                                item.getQuantity(),
                                item.getPrice());
                    })
                    .toList();

            UUID buyerId = order.getUserId();
            User buyer = userService.getUserById(buyerId);
            String firstName = buyer.getFirstName();
            String lastName = buyer.getLastName();

            String status = order.getStatus();

            return new SellerOrderDto(
                    order.getId(),
                    order.getCreatedAt(),
                    order.getTotalAmount(),
                    status,
                    buyerId,
                    firstName,
                    lastName,
                    items);
        }).toList();
    }
}