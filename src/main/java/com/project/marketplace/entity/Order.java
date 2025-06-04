package com.project.marketplace.entity;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "total_amount", nullable = false, scale = 2, precision = 19)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 20)
    private String status; // NEW, PAID, CANCELED

    @Column(name = "payment_id", length = 100)
    private String paymentId;

    @Column(name = "payment_url", length = 255)
    private String paymentUrl;

    @Column(name = "payment_status", length = 20)
    private String paymentStatus;

    @Column(name = "recipient_first_name", length = 100)
    private String recipientFirstName;

    @Column(name = "recipient_last_name", length = 100)
    private String recipientLastName;

    @Column(name = "recipient_phone", length = 20)
    private String recipientPhone;

    @Column(name = "pickup_address", length = 255, nullable = false)
    private String pickupAddress;

    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<OrderItem> items;
}