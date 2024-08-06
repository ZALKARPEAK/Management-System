package com.example.taskmanagmentsystem.dto.Task;

import com.example.taskmanagmentsystem.util.enums.Priority;
import com.example.taskmanagmentsystem.util.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponsePagination {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TaskResponsePagination() {
    }
}