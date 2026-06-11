package com.taskmanager.service;

import com.taskmanager.dto.ProjectDto;
import com.taskmanager.entity.Project;
import com.taskmanager.entity.User;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.exception.UnauthorizedException;
import com.taskmanager.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Page<ProjectDto.Response> getUserProjects(User currentUser, Pageable pageable) {
        return projectRepository.findAllByOwner(currentUser, pageable)
                .map(this::mapToResponse);
    }

    public ProjectDto.Response getProjectById(Long id, User currentUser) {
        Project project = findProjectOrThrow(id);
        validateOwnership(project, currentUser);
        return mapToResponse(project);
    }

    @Transactional
    public ProjectDto.Response createProject(ProjectDto.CreateRequest request, User currentUser) {
        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(currentUser)
                .build();

        return mapToResponse(projectRepository.save(project));
    }

    @Transactional
    public ProjectDto.Response updateProject(Long id, ProjectDto.UpdateRequest request, User currentUser) {
        Project project = findProjectOrThrow(id);
        validateOwnership(project, currentUser);

        if (request.getName() != null) project.setName(request.getName());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getStatus() != null) project.setStatus(request.getStatus());

        return mapToResponse(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(Long id, User currentUser) {
        Project project = findProjectOrThrow(id);
        validateOwnership(project, currentUser);
        projectRepository.delete(project);
    }

    private Project findProjectOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    private void validateOwnership(Project project, User user) {
        if (!project.getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("You don't have access to this project");
        }
    }

    private ProjectDto.Response mapToResponse(Project project) {
        ProjectDto.Response response = new ProjectDto.Response();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setStatus(project.getStatus());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        response.setOwnerId(project.getOwner().getId());
        response.setOwnerEmail(project.getOwner().getEmail());
        response.setTaskCount(project.getTasks() != null ? project.getTasks().size() : 0);
        return response;
    }
}
