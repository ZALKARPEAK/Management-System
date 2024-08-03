package com.example.taskmanagmentsystem.api;

import com.example.taskmanagmentsystem.dto.SimpleResponse;
import com.example.taskmanagmentsystem.dto.Task.TaskRequest;
import com.example.taskmanagmentsystem.dto.Task.TaskResponse;
import com.example.taskmanagmentsystem.service.TaskService;
import com.example.taskmanagmentsystem.util.enums.Status;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskApi {
    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    public SimpleResponse createTask(@RequestBody TaskRequest request) {
        /**
         * Создает новую задачу на основании предоставленных данных в запросе.
         * @param request - объект, содержащий данные для создания задачи.
         * @return SimpleResponse - ответ с информацией о результате операции.
         */
        return taskService.createTask(request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MEMBER', 'GUEST')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача найдена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public TaskResponse getTaskById(@PathVariable Long id) {
        /**
         * Получает задачу по ID.
         * @param id - уникальный идентификатор задачи.
         * @return TaskResponse - объект, содержащий информацию о найденной задаче.
         */
        return taskService.getTaskById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MEMBER', 'GUEST')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список задач успешно получен")
    })
    public List<TaskResponse> getAllTasks() {
        /**
         * Получает список всех задач.
         * @return List<TaskResponse> - список объектов, представляющих задачи.
         */
        return taskService.getAllTasks();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена"),
            @ApiResponse(responseCode = "403", description = "Нет прав для обновления задачи")
    })
    public SimpleResponse updateTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        /**
         * Обновляет существующую задачу на основании предоставленных данных.
         * @param id - уникальный идентификатор задачи, которую нужно обновить.
         * @param request - объект, содержащий новые данные для задачи.
         * @return SimpleResponse - ответ с информацией о результате операции.
         */
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена"),
            @ApiResponse(responseCode = "403", description = "Нет прав для удаления задачи")
    })
    public SimpleResponse deleteTask(@PathVariable Long id) {
        /**
         * Удаляет задачу по ID.
         * @param id - уникальный идентификатор задачи, которую нужно удалить.
         * @return SimpleResponse - ответ с информацией о результате операции.
         */
        return taskService.deleteTask(id);
    }

    @PostMapping("/{taskId}/assign/{assigneeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Исполнитель успешно назначен"),
            @ApiResponse(responseCode = "404", description = "Задача или исполнитель не найдены"),
            @ApiResponse(responseCode = "403", description = "Нет прав для назначения исполнителя")
    })
    public SimpleResponse assignAssignee(@PathVariable Long taskId, @PathVariable Long assigneeId) {
        /**
         * Назначает исполнителя для задачи.
         * @param taskId - уникальный идентификатор задачи, к которой назначается исполнитель.
         * @param assigneeId - уникальный идентификатор исполнителя.
         * @return SimpleResponse - ответ с информацией о результате операции.
         */
        return taskService.assignAssignee(taskId, assigneeId);
    }

    @PutMapping("/{taskId}/change-status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GUEST')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус задачи успешно изменен"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public SimpleResponse changeStatus(@PathVariable Long taskId, @RequestBody Status status) {
        /**
         * Изменяет статус задачи.
         * @param taskId - уникальный идентификатор задачи, статус которой нужно изменить.
         * @param status - новый статус, который нужно установить.
         * @return SimpleResponse - ответ с информацией о результате операции.
         */
        return taskService.changeStatus(taskId, status);
    }
}