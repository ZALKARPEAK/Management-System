package com.example.taskmanagmentsystem.repository;

import com.example.taskmanagmentsystem.dto.Task.TaskResponse;
import com.example.taskmanagmentsystem.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<TaskResponse> findAllByCreatorId(Long id);
}