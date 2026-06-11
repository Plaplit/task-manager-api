package com.taskmanager.controller;

import com.taskmanager.dto.TaskDto;
import com.taskmanager.entity.TaskStatus;
import com.taskmanager.entity.User;
import com.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/projects/{projectId}/tasks")
    @Operation(
        summary = "Get all tasks in a project",
        description = "Returns paginated tasks for a project. Optionally filter by status: TODO, IN_PROGRESS, IN_REVIEW, DONE"
    )
    public ResponseEntity<Page<TaskDto.Response>> getProjectTasks(
            @PathVariable Long projectId,
            @Parameter(description = "Filter by status (optional)", example = "TODO")
            @RequestParam(required = false) TaskStatus status,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(taskService.getProjectTasks(projectId, status, currentUser, pageable));
    }

    @GetMapping("/tasks/my")
    @Operation(
        summary = "Get all tasks related to current user",
        description = "Returns all tasks where you are the ASSIGNEE or the PROJECT OWNER. " +
                      "Includes unassigned tasks in your projects. Optionally filter by status."
    )
    public ResponseEntity<Page<TaskDto.Response>> getMyTasks(
            @AuthenticationPrincipal User currentUser,
            @Parameter(description = "Filter by status (optional)", example = "IN_PROGRESS")
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        return ResponseEntity.ok(taskService.getMyTasks(currentUser, status, pageable));
    }

    @GetMapping("/tasks/{taskId}")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<TaskDto.Response> getTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(taskService.getTaskById(taskId, currentUser));
    }

    @PostMapping("/projects/{projectId}/tasks")
    @Operation(
        summary = "Create a new task in a project",
        description = "Creates a task. Leave assigneeId empty to create an unassigned task. " +
                      "Use GET /api/users to find user IDs for assignment."
    )
    public ResponseEntity<TaskDto.Response> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody TaskDto.CreateRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(projectId, request, currentUser));
    }

    @PostMapping("/tasks/{taskId}/assign/me")
    @Operation(
        summary = "Assign task to yourself",
        description = "Quickly assigns this task to the currently logged-in user. No request body needed."
    )
    public ResponseEntity<TaskDto.Response> assignToMe(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(taskService.assignTaskToMe(taskId, currentUser));
    }

    @DeleteMapping("/tasks/{taskId}/assign")
    @Operation(
        summary = "Remove assignee from task",
        description = "Unassigns the task, making it available for anyone to pick up."
    )
    public ResponseEntity<TaskDto.Response> unassign(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(taskService.unassignTask(taskId, currentUser));
    }

    @PutMapping("/tasks/{taskId}")
    @Operation(
        summary = "Update a task",
        description = "All fields are optional. To reassign, provide a new assigneeId. " +
                      "Use GET /api/users to find user IDs."
    )
    public ResponseEntity<TaskDto.Response> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskDto.UpdateRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request, currentUser));
    }

    @DeleteMapping("/tasks/{taskId}")
    @Operation(summary = "Delete a task")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User currentUser) {
        taskService.deleteTask(taskId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
