package com.project.marketplace.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "good_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodImage {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "good_id", nullable = false)
    private Good good;

    @Column(name = "image_path", length = 255, nullable = false)
    private String imagePath;
}