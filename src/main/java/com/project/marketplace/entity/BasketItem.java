package com.project.marketplace.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

@Entity
@Table(name = "basket_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasketItem {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "UUID")
    private UUID id;

    // Ссылка на корзину, в которой хранится этот элемент
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id", nullable = false)
    private Basket basket;

    // Идентификатор товара, который был добавлен в корзину
    @Column(nullable = false)
    private UUID goodId;

    // Количество товара в корзине
    @Column(nullable = false)
    private int quantity;
}
