package com.project.marketplace.repository;

import com.project.marketplace.entity.GoodImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GoodImageRepository extends JpaRepository<GoodImage, UUID> {
    List<GoodImage> findByGoodId(UUID goodId);

    int countByGoodId(UUID goodId);
}