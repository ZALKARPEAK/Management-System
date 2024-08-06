package com.example.taskmanagmentsystem.service;

import com.example.taskmanagmentsystem.dto.SimpleResponse;
import com.example.taskmanagmentsystem.dto.Task.TaskRequest;
import com.example.taskmanagmentsystem.dto.Task.TaskResponse;
import com.example.taskmanagmentsystem.dto.Task.TaskResponsePagination;
import com.example.taskmanagmentsystem.util.enums.Priority;
import com.example.taskmanagmentsystem.util.enums.Status;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {
    SimpleResponse createTask(TaskRequest request, Priority priority);
    TaskResponse getTaskById(Long id);
    List<TaskResponsePagination> getAllTasks();
    SimpleResponse updateTask(Long id, TaskRequest request, Priority priority);
    SimpleResponse deleteTask(Long id);
    SimpleResponse assignAssignee(Long taskId, String emailMember);
    SimpleResponse changeStatus(Long taskId, Status status);
    Page<TaskResponsePagination> getTasksByCreator(Long creatorId, Status status, int currentPage, int pageSize);
}