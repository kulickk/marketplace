package com.project.marketplace.repository;

import com.project.marketplace.entity.GoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GoodCategoryRepository extends JpaRepository<GoodCategory, UUID> {
}