package com.project.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.marketplace.config.FileStorageProperties;

import jakarta.annotation.PostConstruct;


import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileStorageProperties fileStorageProperties;
    private Path fileStorageLocation;

    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Не удалось создать папку для загрузки: " + ex.getMessage(), ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int idx = originalFilename.lastIndexOf('.');
        if (idx > 0) {
            ext = originalFilename.substring(idx);
        }

        String newFilename = UUID.randomUUID().toString() + ext;

        try {
            if (newFilename.contains("..")) {
                throw new RuntimeException("Недопустимый путь в имени файла " + newFilename);
            }

            Path targetLocation = this.fileStorageLocation.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return newFilename;
        } catch (IOException ex) {
            throw new RuntimeException("Не удалось сохранить файл " + newFilename + ". Пожалуйста, попробуйте снова!", ex);
        }
    }

    public Path loadFileAsResource(String filename) {
        Path filePath = this.fileStorageLocation.resolve(filename).normalize();
        if (Files.exists(filePath)) {
            return filePath;
        } else {
            throw new RuntimeException("Файл не найден " + filename);
        }
    }
}