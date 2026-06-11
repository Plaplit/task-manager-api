package com.taskmanager.service;

import com.taskmanager.dto.TaskDto;
import com.taskmanager.entity.*;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.exception.UnauthorizedException;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public Page<TaskDto.Response> getProjectTasks(Long projectId, TaskStatus status, User currentUser, Pageable pageable) {
        Project project = findProjectOrThrow(projectId);
        validateProjectAccess(project, currentUser);

        if (status != null) {
            return taskRepository.findAllByProjectIdAndStatus(projectId, status, pageable)
                    .map(this::mapToResponse);
        }
        return taskRepository.findAllByProjectId(projectId, pageable)
                .map(this::mapToResponse);
    }

    // Zwraca wszystkie taski gdzie user jest assignee LUB właścicielem projektu
    public Page<TaskDto.Response> getMyTasks(User currentUser, TaskStatus status, Pageable pageable) {
        if (status != null) {
            return taskRepository.findAllRelatedToUserByStatus(currentUser.getId(), status, pageable)
                    .map(this::mapToResponse);
        }
        return taskRepository.findAllRelatedToUser(currentUser.getId(), pageable)
                .map(this::mapToResponse);
    }

    public TaskDto.Response getTaskById(Long taskId, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        validateProjectAccess(task.getProject(), currentUser);
        return mapToResponse(task);
    }

    @Transactional
    public TaskDto.Response createTask(Long projectId, TaskDto.CreateRequest request, User currentUser) {
        Project project = findProjectOrThrow(projectId);
        validateProjectAccess(project, currentUser);

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found with id: " + request.getAssigneeId()));
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM)
                .dueDate(request.getDueDate())
                .project(project)
                .assignee(assignee)
                .build();

        return mapToResponse(taskRepository.save(task));
    }

    @Transactional
    public TaskDto.Response updateTask(Long taskId, TaskDto.UpdateRequest request, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        validateProjectAccess(task.getProject(), currentUser);

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found with id: " + request.getAssigneeId()));
            task.setAssignee(assignee);
        }

        return mapToResponse(taskRepository.save(task));
    }

    // Przypisz task do siebie — dostępne dla właściciela projektu
    @Transactional
    public TaskDto.Response assignTaskToMe(Long taskId, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        validateProjectAccess(task.getProject(), currentUser);
        task.setAssignee(currentUser);
        return mapToResponse(taskRepository.save(task));
    }

    // Usuń przypisanie taska — dostępne dla właściciela projektu
    @Transactional
    public TaskDto.Response unassignTask(Long taskId, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        validateProjectAccess(task.getProject(), currentUser);
        task.setAssignee(null);
        return mapToResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long taskId, User currentUser) {
        Task task = findTaskOrThrow(taskId);
        validateProjectAccess(task.getProject(), currentUser);
        taskRepository.delete(task);
    }

    private Project findProjectOrThrow(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
    }

    private Task findTaskOrThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
    }

    private void validateProjectAccess(Project project, User user) {
        if (!project.getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("You don't have access to this project");
        }
    }

    private TaskDto.Response mapToResponse(Task task) {
        TaskDto.Response response = new TaskDto.Response();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        response.setProjectId(task.getProject().getId());
        response.setProjectName(task.getProject().getName());
        if (task.getAssignee() != null) {
            response.setAssigneeId(task.getAssignee().getId());
            response.setAssigneeEmail(task.getAssignee().getEmail());
            response.setAssigneeFirstName(task.getAssignee().getFirstName());
            response.setAssigneeLastName(task.getAssignee().getLastName());
        }
        return response;
    }
}
