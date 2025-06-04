package com.project.marketplace.service;

import com.project.marketplace.dto.goods.GoodRequest;
import com.project.marketplace.dto.goods.GoodResponse;
import com.project.marketplace.entity.Good;
import com.project.marketplace.entity.GoodCategory;
import com.project.marketplace.entity.GoodImage;
import com.project.marketplace.entity.User;
import com.project.marketplace.repository.GoodsRepository;
import com.project.marketplace.repository.UserRepository;
import com.project.marketplace.repository.GoodCategoryRepository;
import com.project.marketplace.repository.GoodImageRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodsService {

        private final GoodsRepository goodsRepository;
        private final GoodCategoryRepository goodCategoryRepository;
        private final UserRepository userRepository;
        private final GoodImageRepository goodImageRepository;

        @Transactional(readOnly = true)
        public GoodResponse getGoodById(UUID id) {
                Good good = goodsRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Товар не найден"));
                return mapToResponse(good);
        }

        @Transactional(readOnly = true)
        public Good getGoodEntityById(UUID id) {
                return goodsRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Товар не найден"));
        }

        @Transactional
        public void saveGoodEntity(Good good) {
                goodsRepository.save(good);
        }

        @Transactional(readOnly = true)
        public List<GoodResponse> getAllGoods(int page, int size) {
                PageRequest pageReq = PageRequest.of(page, size);
                Page<Good> pageOfGoods = goodsRepository.findAll(pageReq);
                return pageOfGoods.stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<GoodResponse> getGoodsByCategory(UUID categoryId, int page, int size) {
                if (!goodCategoryRepository.existsById(categoryId)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Категория не найдена");
                }
                PageRequest pageReq = PageRequest.of(page, size);
                Page<Good> pageOfGoods = goodsRepository.findByCategoryId(categoryId, pageReq);
                return pageOfGoods.stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Transactional
        public GoodResponse createGood(UUID ownerId, GoodRequest request) {
                GoodCategory category = goodCategoryRepository.findById(request.categoryId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "Категория не найдена"));

                User owner = userRepository.findById(ownerId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "Пользователь не найден"));

                Good good = Good.builder()
                                .name(request.name())
                                .description(request.description())
                                .price(request.price())
                                .stock(request.stock())
                                .brand(request.brand())
                                .category(category)
                                .owner(owner)
                                .build();

                Good saved = goodsRepository.save(good);
                return mapToResponse(saved);
        }

        @Transactional
        public GoodResponse updateGood(UUID id, UUID ownerId, GoodRequest request) {
                Good good = goodsRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Товар не найден"));

                User owner = good.getOwner();
                if (owner == null || !owner.getId().equals(ownerId)) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                        "Нет доступа: этот товар принадлежит другому продавцу");
                }

                GoodCategory category = goodCategoryRepository.findById(request.categoryId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "Категория не найдена"));

                good.setName(request.name());
                good.setDescription(request.description());
                good.setPrice(request.price());
                good.setStock(request.stock());
                good.setBrand(request.brand());
                good.setCategory(category);

                Good saved = goodsRepository.save(good);
                return mapToResponse(saved);
        }

        @Transactional
        public void deleteGood(UUID id, UUID ownerId) {
                Good good = goodsRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Товар не найден"));

                User owner = good.getOwner();
                if (owner == null || !owner.getId().equals(ownerId)) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                        "Нет доступа: этот товар принадлежит другому продавцу");
                }

                goodsRepository.delete(good);
        }

        @Transactional(readOnly = true)
        public List<GoodResponse> getMyGoods(UUID ownerId, int page, int size) {
                PageRequest pageReq = PageRequest.of(page, size);
                Page<Good> pageOfGoods = goodsRepository.findByOwnerId(ownerId, pageReq);
                return pageOfGoods.stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<GoodResponse> searchGoodsByName(String namePart, int page, int size) {
                PageRequest pageReq = PageRequest.of(page, size);
                Page<Good> pageOfGoods = goodsRepository.findByNameContainingIgnoreCase(namePart, pageReq);
                return pageOfGoods.stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        private GoodResponse mapToResponse(Good good) {
                UUID categoryId = good.getCategory() != null ? good.getCategory().getId() : null;
                UUID ownerId = good.getOwner() != null ? good.getOwner().getId() : null;

                List<GoodImage> images = goodImageRepository.findByGoodId(good.getId());
                List<String> paths = images.stream()
                                .map(GoodImage::getImagePath)
                                .collect(Collectors.toList());

                return new GoodResponse(
                                good.getId(),
                                good.getName(),
                                good.getDescription(),
                                good.getPrice(),
                                good.getStock(),
                                good.getBrand(),
                                categoryId,
                                ownerId,
                                paths);
        }
}
