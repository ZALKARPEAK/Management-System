package com.example.taskmanagmentsystem.service.impl;

import com.example.taskmanagmentsystem.dto.Comment.CommentResponse;
import com.example.taskmanagmentsystem.dto.SimpleResponse;
import com.example.taskmanagmentsystem.dto.Task.TaskRequest;
import com.example.taskmanagmentsystem.dto.Task.TaskResponse;
import com.example.taskmanagmentsystem.dto.Task.TaskResponsePagination;
import com.example.taskmanagmentsystem.exception.AlreadyExistsException;
import com.example.taskmanagmentsystem.exception.ForbiddenException;
import com.example.taskmanagmentsystem.exception.NotFoundException;
import com.example.taskmanagmentsystem.model.Task;
import com.example.taskmanagmentsystem.model.UserInfo;
import com.example.taskmanagmentsystem.model.additional.TaskUser;
import com.example.taskmanagmentsystem.repository.TaskRepository;
import com.example.taskmanagmentsystem.repository.UserInfoRepository;
import com.example.taskmanagmentsystem.service.TaskService;
import com.example.taskmanagmentsystem.util.enums.Priority;
import com.example.taskmanagmentsystem.util.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserInfoRepository userInfoRepository;

    @Override
    public SimpleResponse createTask(TaskRequest request, Priority priority) {
        UserInfo creator = getCurrentUser();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(priority);
        task.setStatus(Status.TODO);
        task.setCreator(creator);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
        return new SimpleResponse("Задача успешно создана", HttpStatus.CREATED);
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Задача не найдена"));
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
    public List<TaskResponsePagination> getAllTasks() {
        UserInfo user = getCurrentUser();
        List<Task> tasks = taskRepository.findAllByCreatorId(user.getId());

        return tasks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SimpleResponse updateTask(Long taskId, TaskRequest request, Priority priority) {
        UserInfo user = getCurrentUser();
        Task task = getTaskIfAuthorized(user, taskId);

        if (user.getCreatedTasks().stream().noneMatch(task1 -> task1.getId().equals(task.getId()))) {
            throw new ForbiddenException("У вас нет прав на удаление этой задачи");
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(priority);
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
        return new SimpleResponse("Задача успешно обновлена", HttpStatus.OK);
    }

    @Override
    public SimpleResponse deleteTask(Long taskId) {
        UserInfo user = getCurrentUser();
        Task task = getTaskIfAuthorized(user, taskId);

        if (user.getCreatedTasks().stream().noneMatch(task1 -> task1.getId().equals(task.getId()))
                && task.getUsers().stream().noneMatch(taskUser -> taskUser.getUserId().equals(user.getId()))) {
            throw new ForbiddenException("У вас нет прав на удаление этой задачи");
        }

        taskRepository.delete(task);

        return new SimpleResponse("Задача успешно удалена", HttpStatus.OK);
    }

    @Override
    public SimpleResponse assignAssignee(Long taskId, String emailMember) {
        UserInfo admin = getCurrentUser();
        Task task = getTaskIfAuthorized(admin, taskId);

        if (admin.getCreatedTasks().stream().noneMatch(task1 -> task1.getId().equals(task.getId()))) {
            throw new ForbiddenException("У вас нет прав на удаление этой задачи");
        }

        UserInfo member = userInfoRepository.getUserAccountByEmail(emailMember).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));

        if (task.getUsers().stream()
                .anyMatch(taskUser -> taskUser.getUserId().equals(member.getId()))) {
            throw new AlreadyExistsException("Этот пользователь уже назначен на эту задачу");
        }

        TaskUser taskUser = new TaskUser(member.getId(), member.getUserInfoUserName());

        Set<TaskUser> users = new HashSet<>(task.getUsers());
        users.add(taskUser);

        task.setUsers(users);
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

    @Override
    public Page<TaskResponsePagination> getTasksByCreator(Long creatorId, Status status, int currentPage, int pageSize) {
        UserInfo creator = userInfoRepository.findById(creatorId).orElseThrow(() ->
                new NotFoundException("Создатель не найден"));

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "updatedAt"));

        Page<Task> taskPage = taskRepository.findByCreatorAndStatus(creator, status, pageable);

        return taskPage.map(this::convertToResponse);
    }

    private TaskResponsePagination convertToResponse(Task task) {
        TaskResponsePagination response = new TaskResponsePagination();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        return response;
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