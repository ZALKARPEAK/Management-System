package com.example.taskmanagmentsystem.dto.Task;

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
}