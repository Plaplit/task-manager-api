package com.taskmanager.controller;

import com.taskmanager.entity.User;
import com.taskmanager.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User lookup endpoints")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    @Operation(
        summary = "Get all users",
        description = "Returns a list of all users with their IDs. Use these IDs as assigneeId when creating or updating tasks."
    )
    public ResponseEntity<List<UserSummary>> getAllUsers() {
        List<UserSummary> users = userRepository.findAll().stream()
                .map(u -> new UserSummary(u.getId(), u.getEmail(), u.getFirstName(), u.getLastName()))
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current logged-in user info")
    public ResponseEntity<UserSummary> getCurrentUser(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(
            new UserSummary(currentUser.getId(), currentUser.getEmail(),
                            currentUser.getFirstName(), currentUser.getLastName())
        );
    }

    @Data
    public static class UserSummary {
        private final Long id;
        private final String email;
        private final String firstName;
        private final String lastName;
    }
}
