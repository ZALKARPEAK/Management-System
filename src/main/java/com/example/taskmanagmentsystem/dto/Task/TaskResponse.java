package com.example.taskmanagmentsystem.dto.Task;

import com.example.taskmanagmentsystem.dto.Comment.CommentResponse;
import com.example.taskmanagmentsystem.util.enums.Priority;
import com.example.taskmanagmentsystem.util.enums.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentResponse> comments;

    public TaskResponse(Long id, String title, String description, Status status,
                        Priority priority, LocalDateTime createdAt, LocalDateTime updatedAt,
                        List<CommentResponse> comments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.comments = comments;
    }
}