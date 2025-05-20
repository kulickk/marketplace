package com.project.marketplace.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "basket")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Basket {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "UUID")
    private UUID id;

    // Уникальный идентификатор пользователя (каждый пользователь имеет одну
    // корзину)
    @Column(nullable = false, unique = true)
    private UUID userId;

    // Список элементов корзины
    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BasketItem> items = new ArrayList<>();
}
