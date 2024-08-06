package com.example.taskmanagmentsystem.service;

import com.example.taskmanagmentsystem.dto.Comment.CommentResponse;
import com.example.taskmanagmentsystem.dto.SimpleResponse;

import java.util.List;

public interface CommentService {
    SimpleResponse createComment(Long taskId, String content);
    List<CommentResponse> getCommentsByTask(Long taskId);
    SimpleResponse updateComment(Long commentId, String content);
    SimpleResponse deleteComment(Long commentId);
}