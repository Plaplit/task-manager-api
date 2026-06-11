package com.taskmanager.dto;

import com.taskmanager.entity.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

public class ProjectDto {

    @Data
    public static class CreateRequest {

        @NotBlank(message = "Project name is required")
        @Schema(description = "Name of the project", example = "Mobile App")
        private String name;

        @Schema(description = "Optional description", example = "A cross-platform mobile application")
        private String description;
    }

    @Data
    public static class UpdateRequest {

        @Schema(description = "New project name", example = "Mobile App v2")
        private String name;

        @Schema(description = "New description")
        private String description;

        @Schema(description = "New status", example = "ACTIVE", allowableValues = {"ACTIVE", "COMPLETED", "ARCHIVED"})
        private ProjectStatus status;
    }

    @Data
    public static class Response {
        private Long id;
        private String name;
        private String description;
        private ProjectStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long ownerId;
        private String ownerEmail;
        private int taskCount;
    }
}
