package com.project.marketplace.service;

import com.project.marketplace.dto.goodcategories.GoodCategoryRequest;
import com.project.marketplace.dto.goodcategories.GoodCategoryResponse;
import com.project.marketplace.entity.GoodCategory;
import com.project.marketplace.repository.GoodCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodCategoryService {

    private final GoodCategoryRepository repository;

    @Transactional(readOnly = true)
    public GoodCategoryResponse getCategory(UUID id) {
        GoodCategory category = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Категория не найдена"));
        return mapToResponse(category);
    }

    @Transactional(readOnly = true)
    public List<GoodCategoryResponse> getAllCategories() {
        return repository.findAll().stream()
                .filter(c -> c.getParent() == null)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public GoodCategoryResponse createCategory(GoodCategoryRequest request) {
        GoodCategory category = GoodCategory.builder()
                .name(request.name())
                .description(request.description())
                .build();

        if (request.parentId() != null) {
            GoodCategory parent = repository.findById(request.parentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Родительская категория не найдена"));
            category.setParent(parent);
        }

        GoodCategory saved = repository.save(category);
        return mapToResponse(saved);
    }

    @Transactional
    public GoodCategoryResponse updateCategory(UUID id, GoodCategoryRequest request) {
        GoodCategory category = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Категория не найдена"));

        category.setName(request.name());
        category.setDescription(request.description());

        if (request.parentId() != null) {
            GoodCategory parent = repository.findById(request.parentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Родительская категория не найдена"));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        GoodCategory saved = repository.save(category);
        return mapToResponse(saved);
    }

    @Transactional
    public void deleteCategory(UUID id) {
        GoodCategory category = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Категория не найдена"));
        repository.delete(category);
    }

    private GoodCategoryResponse mapToResponse(GoodCategory category) {
        UUID parentId = category.getParent() != null ? category.getParent().getId() : null;
        List<GoodCategoryResponse> children = category.getChildren() == null ? List.of()
                : category.getChildren().stream()
                        .map(this::mapToResponse)
                        .collect(Collectors.toList());
        return new GoodCategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                parentId,
                children);
    }
}
