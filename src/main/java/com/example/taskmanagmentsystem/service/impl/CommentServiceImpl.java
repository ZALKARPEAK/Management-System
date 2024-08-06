package com.example.taskmanagmentsystem.service.impl;

import com.example.taskmanagmentsystem.dto.Comment.CommentResponse;
import com.example.taskmanagmentsystem.dto.SimpleResponse;
import com.example.taskmanagmentsystem.exception.ForbiddenException;
import com.example.taskmanagmentsystem.exception.NotFoundException;
import com.example.taskmanagmentsystem.model.Comment;
import com.example.taskmanagmentsystem.model.Task;
import com.example.taskmanagmentsystem.model.UserInfo;
import com.example.taskmanagmentsystem.repository.CommentRepository;
import com.example.taskmanagmentsystem.repository.TaskRepository;
import com.example.taskmanagmentsystem.repository.UserInfoRepository;
import com.example.taskmanagmentsystem.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserInfoRepository userInfoRepository;

    @Override
    public SimpleResponse createComment(Long taskId, String content) {
        UserInfo userInfo = getCurrentUser();

        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new NotFoundException("Задача не найдена"));

        if (task.getUsers().stream()
                .noneMatch(taskUser -> taskUser.getUserId().equals(userInfo.getId()))) {
            throw new ForbiddenException("У вас нет доступа к комментариям этой задачи");
        }

        Comment comment = new Comment();
        comment.setAuthor(userInfo);
        comment.setTask(task);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return new SimpleResponse("Комментарий успешно создана", HttpStatus.CREATED);
    }

    @Override
    public List<CommentResponse> getCommentsByTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new NotFoundException("Задача не найдена"));

        List<Comment> comments = commentRepository.findByTask_Id(task.getId());
        return comments.stream()
                .map(this::convertToCommentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SimpleResponse updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий не найден"));

        comment.setContent(content);

        UserInfo currentUser = getCurrentUser();

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("У вас нет прав на удаление этого комментария");
        }

        commentRepository.save(comment);

        return new SimpleResponse("Комментарий успешно обновлен", HttpStatus.OK);
    }

    @Override
    public SimpleResponse deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий не найден"));

        UserInfo currentUser = getCurrentUser();

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("У вас нет прав на удаление этого комментария");
        }

        commentRepository.delete(comment);

        return new SimpleResponse("Комментарий успешно удален", HttpStatus.OK);
    }

    private CommentResponse convertToCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt());
    }

    private UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userInfoRepository.getUserAccountByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
