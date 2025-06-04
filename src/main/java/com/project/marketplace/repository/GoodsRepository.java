package com.project.marketplace.repository;

import com.project.marketplace.entity.Good;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GoodsRepository extends JpaRepository<Good, UUID> {
    List<Good> findByCategoryId(UUID categoryId);

    List<Good> findByOwnerId(UUID ownerId);

    List<Good> findByNameContainingIgnoreCase(String substring);

    Page<Good> findAll(Pageable pageable);

    Page<Good> findByNameContainingIgnoreCase(String namePart, Pageable pageable);

    Page<Good> findByCategoryId(UUID categoryId, Pageable pageable);

    Page<Good> findByOwnerId(UUID ownerId, Pageable pageable);
}
