package com.example.taskmanagmentsystem.dto.Task;

import com.example.taskmanagmentsystem.util.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class TaskRequest {
    private String title;
    private String description;
    private Priority priority;
}
