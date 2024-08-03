package com.example.taskmanagmentsystem.service.impl;

import com.example.taskmanagmentsystem.dto.Comment.CommentResponse;
import com.example.taskmanagmentsystem.dto.SimpleResponse;
import com.example.taskmanagmentsystem.dto.Task.TaskRequest;
import com.example.taskmanagmentsystem.dto.Task.TaskResponse;
import com.example.taskmanagmentsystem.exception.ForbiddenException;
import com.example.taskmanagmentsystem.exception.NotFoundException;
import com.example.taskmanagmentsystem.model.Task;
import com.example.taskmanagmentsystem.model.UserInfo;
import com.example.taskmanagmentsystem.repository.TaskRepository;
import com.example.taskmanagmentsystem.repository.UserInfoRepository;
import com.example.taskmanagmentsystem.service.TaskService;
import com.example.taskmanagmentsystem.util.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserInfoRepository userInfoRepository;

    @Override
    public SimpleResponse createTask(TaskRequest request) {
        UserInfo creator = getCurrentUser();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(Status.TODO);
        task.setCreator(creator);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
        return new SimpleResponse("Задача успешно создана", HttpStatus.CREATED);
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Задача не найдена"));
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .comments(task.getComments().stream()
                        .map(comment -> CommentResponse.builder()
                                .id(comment.getId())
                                .content(comment.getContent())
                                .createdAt(comment.getCreatedAt())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        UserInfo user = getCurrentUser();

        return taskRepository.findAllByCreatorId(user.getId()).stream()
                .map(task -> TaskResponse.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .status(task.getStatus())
                        .priority(task.getPriority())
                        .createdAt(task.getCreatedAt())
                        .updatedAt(task.getUpdatedAt())
                        .comments(task.getComments().stream()
                                .map(comment -> CommentResponse.builder()
                                        .id(comment.getId())
                                        .content(comment.getContent())
                                        .createdAt(comment.getCreatedAt())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public SimpleResponse updateTask(Long taskId, TaskRequest request) {
        UserInfo user = getCurrentUser();
        Task task = getTaskIfAuthorized(user, taskId);

        if (user.getCreatedTasks().stream().noneMatch(task1 -> task1.getId().equals(task.getId()))) {
            throw new ForbiddenException("У вас нет прав на удаление этой задачи");
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
        return new SimpleResponse("Задача успешно обновлена", HttpStatus.OK);
    }

    @Override
    public SimpleResponse deleteTask(Long taskId) {
        UserInfo user = getCurrentUser();
        Task task = getTaskIfAuthorized(user, taskId);

        if (user.getCreatedTasks().stream().noneMatch(task1 -> task1.getId().equals(task.getId()))) {
            throw new ForbiddenException("У вас нет прав на удаление этой задачи");
        }
        taskRepository.delete(task);
        return new SimpleResponse("Задача успешно удалена", HttpStatus.OK);
    }

    @Override
    public SimpleResponse assignAssignee(Long taskId, Long assigneeId) {
        UserInfo admin = getCurrentUser();
        Task task = getTaskIfAuthorized(admin, taskId);

        if (admin.getCreatedTasks().stream().noneMatch(task1 -> task1.getId().equals(task.getId()))) {
            throw new ForbiddenException("У вас нет прав на удаление этой задачи");
        }

        UserInfo member = userInfoRepository.findById(assigneeId).orElseThrow(() ->
                new RuntimeException("Исполнитель не найден"));
        task.setAssignee(member);
        taskRepository.save(task);

        return new SimpleResponse("Исполнитель успешно назначен", HttpStatus.OK);
    }

    @Override
    public SimpleResponse changeStatus(Long taskId, Status status) {
        UserInfo admin = getCurrentUser();
        Task task = getTaskIfAuthorized(admin, taskId);

        if (admin.getCreatedTasks().stream().noneMatch(task1 -> task1.getId().equals(task.getId()))) {
            throw new ForbiddenException("У вас нет прав на удаление этой задачи");
        }

        task.setStatus(status);
        taskRepository.save(task);
        return new SimpleResponse("Статус задачи успешно изменен", HttpStatus.OK);
    }

    private UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userInfoRepository.getUserAccountByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }


    private Task getTaskIfAuthorized(UserInfo user, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        if (user.getCreatedTasks().stream().noneMatch(task1 -> task1.getId().equals(task.getId()))) {
            throw new ForbiddenException("У вас нет прав на выполнение данной операции с задачей");
        }

        return task;
    }
}