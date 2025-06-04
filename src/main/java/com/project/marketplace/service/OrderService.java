package com.project.marketplace.service;

import com.project.marketplace.dto.basket.BasketItemResponse;
import com.project.marketplace.dto.order.OrderItemDto;
import com.project.marketplace.dto.order.OrderRequest;
import com.project.marketplace.dto.order.OrderResponse;
import com.project.marketplace.entity.BasketItem;
import com.project.marketplace.entity.Good;
import com.project.marketplace.entity.GoodImage;
import com.project.marketplace.entity.Order;
import com.project.marketplace.entity.OrderItem;
import com.project.marketplace.entity.User;
import com.project.marketplace.repository.BasketItemRepository;
import com.project.marketplace.repository.GoodImageRepository;
import com.project.marketplace.repository.GoodsRepository;
import com.project.marketplace.repository.OrderRepository;
import com.project.marketplace.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final BasketItemRepository basketItemRepository;
    private final GoodsRepository goodsRepository;
    private final GoodImageRepository goodImageRepository;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(UUID userId, OrderRequest orderRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Пользователь не найден"));

        List<UUID> basketItemIds = orderRequest.basketItemIds();
        List<BasketItem> basketItems = basketItemIds.stream()
                .map(id -> basketItemRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Элемент корзины " + id + " не найден")))
                .peek(bi -> {
                    if (!bi.getBasket().getUserId().equals(userId)) {
                        throw new ResponseStatusException(
                                HttpStatus.FORBIDDEN,
                                "Элемент корзины " + bi.getId() + " не принадлежит текущему пользователю");
                    }
                })
                .collect(Collectors.toList());

        if (basketItems.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Нужно хотя бы один элемент корзины для заказа");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalSum = BigDecimal.ZERO;

        for (BasketItem bi : basketItems) {
            UUID goodId = bi.getGoodId();
            int quantity = bi.getQuantity();

            Good good = goodsRepository.findById(goodId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Товар " + goodId + " не найден"));

            if (good.getStock() < quantity) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Недостаточно товара \"" + good.getName() +
                                "\" на складе. Доступно: " + good.getStock() +
                                ", требуется: " + quantity);
            }

            good.setStock(good.getStock() - quantity);
            goodsRepository.save(good);

            BigDecimal pricePerUnit = good.getPrice();
            totalSum = totalSum.add(pricePerUnit.multiply(BigDecimal.valueOf(quantity)));

            OrderItem oi = OrderItem.builder()
                    .goodId(goodId)
                    .quantity(quantity)
                    .price(pricePerUnit)
                    .build();
            orderItems.add(oi);
        }

        Order order = Order.builder()
                .userId(userId)
                .totalAmount(totalSum)
                .status("NEW")
                .recipientFirstName(
                        orderRequest.recipientFirstName() != null
                                ? orderRequest.recipientFirstName()
                                : user.getFirstName())
                .recipientLastName(
                        orderRequest.recipientLastName() != null
                                ? orderRequest.recipientLastName()
                                : user.getLastName())
                .recipientPhone(
                        orderRequest.recipientPhone() != null
                                ? orderRequest.recipientPhone()
                                : user.getPhoneNumber())
                .pickupAddress(orderRequest.pickupAddress())
                .build();


        orderItems.forEach(oi -> oi.setOrder(order));
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        Map<String, Object> paymentResult = paymentService.payOrder(
                savedOrder.getTotalAmount(),
                savedOrder.getId().toString());

        String paymentId = (String) paymentResult.get("id");
        @SuppressWarnings("unchecked")
        Map<String, Object> confirmation = (Map<String, Object>) paymentResult.get("confirmation");
        String paymentUrl = (String) confirmation.get("confirmation_token");
        String paymentStatus = (String) paymentResult.get("status");

        savedOrder.setPaymentId(paymentId);
        savedOrder.setPaymentUrl(paymentUrl);
        savedOrder.setPaymentStatus(paymentStatus);
        savedOrder.setStatus("NEW");
        orderRepository.save(savedOrder);

        List<OrderItemDto> itemDtos = savedOrder.getItems().stream()
                .map(oi -> {
                    Good g = goodsRepository.findById(oi.getGoodId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST, "Товар " + oi.getGoodId() + " не найден"));

                    List<GoodImage> imgs = goodImageRepository.findByGoodId(g.getId());
                    String img = imgs.isEmpty() ? null : imgs.get(0).getImagePath();

                    return new OrderItemDto(
                            oi.getId(),
                            oi.getGoodId(),
                            g.getName(),
                            oi.getPrice(),
                            oi.getQuantity(),
                            img);
                })
                .collect(Collectors.toList());

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getCreatedAt(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus(),
                savedOrder.getPaymentId(),
                savedOrder.getPaymentUrl(),
                savedOrder.getPaymentStatus(),
                savedOrder.getRecipientFirstName(),
                savedOrder.getRecipientLastName(),
                savedOrder.getRecipientPhone(),
                savedOrder.getPickupAddress(),
                itemDtos);
    }

    @Transactional
    public List<OrderResponse> getOrdersForUser(UUID userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(order -> {
                    List<OrderItemDto> itemDtos = order.getItems().stream()
                            .map(oi -> {
                                Good g = goodsRepository.findById(oi.getGoodId())
                                        .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST,
                                                "Товар " + oi.getGoodId() + " не найден"));

                                List<GoodImage> imgs = goodImageRepository.findByGoodId(g.getId());
                                String img = imgs.isEmpty() ? null : imgs.get(0).getImagePath();

                                return new OrderItemDto(
                                        oi.getId(),
                                        oi.getGoodId(),
                                        g.getName(),
                                        oi.getPrice(),
                                        oi.getQuantity(),
                                        img);
                            })
                            .collect(Collectors.toList());

                    return new OrderResponse(
                            order.getId(),
                            order.getUserId(),
                            order.getCreatedAt(),
                            order.getTotalAmount(),
                            order.getStatus(),
                            order.getPaymentId(),
                            order.getPaymentUrl(),
                            order.getPaymentStatus(),
                            order.getRecipientFirstName(),
                            order.getRecipientLastName(),
                            order.getRecipientPhone(),
                            order.getPickupAddress(),
                            itemDtos);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse getOrder(UUID userId, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Заказ не найден"));

        if (!order.getUserId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Нет доступа к этому заказу");
        }

        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(oi -> {
                    Good g = goodsRepository.findById(oi.getGoodId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    "Товар " + oi.getGoodId() + " не найден"));

                    List<GoodImage> imgs = goodImageRepository.findByGoodId(g.getId());
                    String img = imgs.isEmpty() ? null : imgs.get(0).getImagePath();

                    return new OrderItemDto(
                            oi.getId(),
                            oi.getGoodId(),
                            g.getName(),
                            oi.getPrice(),
                            oi.getQuantity(),
                            img);
                })
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getCreatedAt(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getPaymentId(),
                order.getPaymentUrl(),
                order.getPaymentStatus(),
                order.getRecipientFirstName(),
                order.getRecipientLastName(),
                order.getRecipientPhone(),
                order.getPickupAddress(),
                itemDtos);
    }
}