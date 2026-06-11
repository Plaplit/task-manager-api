package com.taskmanager.repository;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAllByProjectId(Long projectId, Pageable pageable);
    Page<Task> findAllByProjectIdAndStatus(Long projectId, TaskStatus status, Pageable pageable);
    Page<Task> findAllByAssigneeId(Long assigneeId, Pageable pageable);

    // Zwraca taski gdzie user jest assignee LUB właścicielem projektu
    @Query("SELECT t FROM Task t WHERE t.assignee.id = :userId OR t.project.owner.id = :userId")
    Page<Task> findAllRelatedToUser(@Param("userId") Long userId, Pageable pageable);

    // Jak wyżej ale z filtrem po statusie
    @Query("SELECT t FROM Task t WHERE (t.assignee.id = :userId OR t.project.owner.id = :userId) AND t.status = :status")
    Page<Task> findAllRelatedToUserByStatus(@Param("userId") Long userId, @Param("status") TaskStatus status, Pageable pageable);
}
