package com.example.taskmanagmentsystem.service;

import com.example.taskmanagmentsystem.dto.SimpleResponse;
import com.example.taskmanagmentsystem.dto.Task.TaskRequest;
import com.example.taskmanagmentsystem.dto.Task.TaskResponse;
import com.example.taskmanagmentsystem.util.enums.Status;

import java.util.List;

public interface TaskService {
    SimpleResponse createTask(TaskRequest request);
    TaskResponse getTaskById(Long id);
    List<TaskResponse> getAllTasks();
    SimpleResponse updateTask(Long id, TaskRequest request);
    SimpleResponse deleteTask(Long id);
    SimpleResponse assignAssignee(Long taskId, Long assigneeId);
    SimpleResponse changeStatus(Long taskId, Status status);
}