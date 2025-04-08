package com.project.marketplace.controller;

import com.project.marketplace.dto.auth.RegisterRequest;
import com.project.marketplace.entity.User;
import com.project.marketplace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<User> register(@RequestBody @Valid RegisterRequest request) {
        User user = userService.createUser(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Получить пользователя по ID")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "UUID пользователя", example = "cb3e0f73-c81f-42f3-a96e-bb5934180150") @PathVariable("id") @NotNull UUID id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Получить пользователя по Email")
    public ResponseEntity<User> getUserByEmail(
            @Parameter(description = "Email пользователя", example = "user@example.com") @PathVariable("email") @NotBlank @Email String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
}
