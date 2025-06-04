package com.project.marketplace.service;

import com.project.marketplace.dto.admin.AdminUserDto;
import com.project.marketplace.entity.User;
import com.project.marketplace.model.Role;
import com.project.marketplace.repository.UserRepository;
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
public class AdminService {

    private final UserRepository userRepository;
    private final UserService userService;

    public void checkIsAdmin(UUID currentUserId) {
        User me = userService.getUserById(currentUserId);
        if (me.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Только администратор имеет доступ к этому ресурсу");
        }
    }

    @Transactional(readOnly = true)
    public List<AdminUserDto> listAllUsers() {
        return userRepository.findAll().stream()
            .map(u -> new AdminUserDto(
                u.getId(),
                u.getFirstName(),
                u.getLastName(),
                u.getEmail(),
                u.getGender().name(),
                u.getRole().name(),
                u.getPhoneNumber(),
                u.getCreatedAt()
            ))
            .collect(Collectors.toList());
    }

    @Transactional
    public void promoteToSeller(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Пользователь не найден"));
        user.setRole(Role.SELLER);
        userRepository.save(user);
    }
}