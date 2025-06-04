package com.project.marketplace.controller;

import com.project.marketplace.dto.admin.AdminUserDto;
import com.project.marketplace.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    private UUID getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Пользователь не авторизован");
        }
        Object raw = session.getAttribute("userId");
        if (raw == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Пользователь не авторизован");
        }
        return UUID.fromString(raw.toString());
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDto>> getAllUsers(HttpServletRequest request) {
        UUID user = getCurrentUserId(request);
        adminService.checkIsAdmin(user);
        List<AdminUserDto> all = adminService.listAllUsers();
        return ResponseEntity.ok(all);
    }

    @PatchMapping("/users/{id}/promote-seller")
    public ResponseEntity<Void> promoteSeller(
            HttpServletRequest request,
            @PathVariable("id") UUID userIdToPromote) {

        UUID user = getCurrentUserId(request);
        adminService.checkIsAdmin(user);
        adminService.promoteToSeller(userIdToPromote);
        return ResponseEntity.noContent().build();
    }
}