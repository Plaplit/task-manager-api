package com.taskmanager.dto;

import com.taskmanager.entity.TaskPriority;
import com.taskmanager.entity.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskDto {

    @Data
    public static class CreateRequest {

        @NotBlank(message = "Task title is required")
        @Schema(description = "Title of the task", example = "Design login screen")
        private String title;

        @Schema(description = "Detailed description", example = "Create mockups and implement the UI")
        private String description;

        @Schema(description = "Priority level", example = "MEDIUM", allowableValues = {"LOW", "MEDIUM", "HIGH", "CRITICAL"})
        private TaskPriority priority;

        @Schema(description = "Due date in format YYYY-MM-DD", example = "2025-12-31")
        private LocalDate dueDate;

        @Schema(description = "ID of the user to assign this task to (use GET /api/users to find IDs)", example = "1")
        private Long assigneeId;
    }

    @Data
    public static class UpdateRequest {

        @Schema(description = "New title", example = "Redesign login screen")
        private String title;

        @Schema(description = "New description")
        private String description;

        @Schema(description = "New status", example = "IN_PROGRESS", allowableValues = {"TODO", "IN_PROGRESS", "IN_REVIEW", "DONE"})
        private TaskStatus status;

        @Schema(description = "New priority", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH", "CRITICAL"})
        private TaskPriority priority;

        @Schema(description = "New due date in format YYYY-MM-DD", example = "2025-12-31")
        private LocalDate dueDate;

        @Schema(description = "ID of the user to reassign this task to", example = "2")
        private Long assigneeId;
    }

    @Data
    public static class Response {
        private Long id;
        private String title;
        private String description;
        private TaskStatus status;
        private TaskPriority priority;
        private LocalDate dueDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long projectId;
        private String projectName;
        private Long assigneeId;
        private String assigneeEmail;
        private String assigneeFirstName;
        private String assigneeLastName;
    }
}
