package com.example.taskmanagmentsystem.dto.Comment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
}