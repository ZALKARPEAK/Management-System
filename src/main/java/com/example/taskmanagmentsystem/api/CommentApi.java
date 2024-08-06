package com.example.taskmanagmentsystem.api;

import com.example.taskmanagmentsystem.dto.Comment.CommentResponse;
import com.example.taskmanagmentsystem.dto.SimpleResponse;
import com.example.taskmanagmentsystem.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentApi {

    private final CommentService commentService;

    @PostMapping("/create-comment/{taskId}")
    @PreAuthorize("isAuthenticated()")
    public SimpleResponse createComment(
            @PathVariable Long taskId,
            @RequestBody String content) {
        /**
         * Создает новый комментарий к задаче.
         * @param taskId - ID задачи.
         * @param content - Текст комментария.
         * @return SimpleResponse - Ответ с информацией об успехе операции.
         */
        return commentService.createComment(taskId, content);
    }

    @GetMapping("/get-comment/{taskId}")
    @PreAuthorize("isAuthenticated()")
    public List<CommentResponse> getCommentsByTask(
            @PathVariable Long taskId) {
        /**
         * Возвращает список комментариев к задаче.
         * @param taskId - ID задачи.
         * @return List<CommentResponse> - Список комментариев к задаче.
         */
        return commentService.getCommentsByTask(taskId);
    }

    @PutMapping("/edit-comment/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public SimpleResponse updateComment(
            @PathVariable Long commentId,
            @RequestBody String content) {
        /**
         * Обновляет текст комментария.
         * @param commentId - ID комментария.
         * @param content - Новый текст комментария.
         * @return SimpleResponse - Ответ с информацией об успехе операции.
         */
        return commentService.updateComment(commentId, content);
    }

    @DeleteMapping("/delete-comment/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public SimpleResponse deleteComment(
            @PathVariable Long commentId) {
        /**
         * Удаляет комментарий по его ID.
         * @param commentId - ID комментария.
         * @return SimpleResponse - Ответ с информацией об успехе операции.
         */
        return commentService.deleteComment(commentId);
    }
}