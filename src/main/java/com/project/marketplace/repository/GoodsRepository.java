package com.project.marketplace.repository;

import com.project.marketplace.entity.Good;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GoodsRepository extends JpaRepository<Good, UUID> {
    List<Good> findByCategoryId(UUID categoryId);
}
