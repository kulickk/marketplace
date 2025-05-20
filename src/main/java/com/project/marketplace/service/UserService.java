package com.project.marketplace.service;

import com.project.marketplace.dto.auth.UserResponse;
import com.project.marketplace.dto.user.ChangePasswordRequest;
import com.project.marketplace.dto.user.UpdateProfileRequest;
import com.project.marketplace.entity.Basket;
import com.project.marketplace.entity.User;
import com.project.marketplace.exceptions.EmailAlreadyExistsException;
import com.project.marketplace.exceptions.UserNotFoundException;
import com.project.marketplace.model.Gender;
import com.project.marketplace.model.Role;
import com.project.marketplace.repository.BasketRepository;
import com.project.marketplace.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BasketRepository basketRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(String email, String password, String firstName, String lastName) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .gender(Gender.NOT_STATED)
                .role(Role.BUYER)
                .build();

        User savedUser = userRepository.save(user);

        // 3. После сохранения пользователя создаём корзину с его userId
        Basket basket = Basket.builder()
                .userId(savedUser.getId())
                .build();
        Basket savedBasket = basketRepository.save(basket);

        // 4. Обновляем пользователя, устанавливая ссылку на корзину
        savedUser.setBasket(savedBasket);
        userRepository.save(savedUser);

        return toResponse(savedUser);
    }

    public UserResponse getProfile(HttpServletRequest req) {
        User user = findCurrentUser(req);
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateProfile(HttpServletRequest req, UpdateProfileRequest dto) {
        User u = findCurrentUser(req);
        u.setFirstName(dto.firstName());
        u.setLastName(dto.lastName());
        u.setGender(dto.gender());
        return toResponse(userRepository.save(u));
    }

    @Transactional
    public void changePassword(HttpServletRequest req, ChangePasswordRequest dto) {
        User u = findCurrentUser(req);
        if (!passwordEncoder.matches(dto.currentPassword(), u.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный текущий пароль");
        }
        u.setPasswordHash(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(u);
    }

    private User findCurrentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Нет активной сессии");
        }

        Object userIdAttr = session.getAttribute("userId");
        if (userIdAttr == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не авторизован");
        }

        UUID userId;
        try {
            userId = UUID.fromString(userIdAttr.toString());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный формат userId в сессии");
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не найден"));
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(
                u.getId(),
                u.getEmail(),
                u.getFirstName(),
                u.getLastName(),
                u.getGender().name(),
                u.getCreatedAt(),
                u.getUpdatedAt());
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with Email " + email + " not found"));
    }
}