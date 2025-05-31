package com.project.marketplace.controller;

import com.project.marketplace.dto.auth.UserResponse;
import com.project.marketplace.dto.user.ChangePasswordRequest;
import com.project.marketplace.dto.user.UpdateProfileRequest;
import com.project.marketplace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Получение профиля текущего пользователя.
     */
    @GetMapping
    @Operation(summary = "Получить профиль текущего пользователя")
    public ResponseEntity<UserResponse> getMyProfile(HttpServletRequest request) {
        UserResponse profile = userService.getProfile(request);
        return ResponseEntity.ok(profile);
    }

    /**
     * Обновление имени, фамилии и пола.
     */
    @PatchMapping("/profile")
    @Operation(summary = "Обновить имя, фамилию и пол пользователя")
    public ResponseEntity<UserResponse> updateProfile(
            HttpServletRequest request,
            @RequestBody @Valid UpdateProfileRequest body) {
        UserResponse updated = userService.updateProfile(request, body);
        return ResponseEntity.ok(updated);
    }

    /**
     * Смена пароля: нужно указать старый и новый.
     */
    @PatchMapping("/password")
    @Operation(summary = "Сменить пароль пользователя")
    public ResponseEntity<Void> changePassword(
            HttpServletRequest request,
            @RequestBody @Valid ChangePasswordRequest body) {
        userService.changePassword(request, body);
        return ResponseEntity.noContent().build();
    }
}
