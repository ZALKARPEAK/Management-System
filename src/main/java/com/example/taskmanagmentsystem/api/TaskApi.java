package com.example.taskmanagmentsystem.api;

import com.example.taskmanagmentsystem.dto.SimpleResponse;
import com.example.taskmanagmentsystem.dto.Task.TaskRequest;
import com.example.taskmanagmentsystem.dto.Task.TaskResponse;
import com.example.taskmanagmentsystem.dto.Task.TaskResponsePagination;
import com.example.taskmanagmentsystem.service.TaskService;
import com.example.taskmanagmentsystem.util.enums.Priority;
import com.example.taskmanagmentsystem.util.enums.Status;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin
public class TaskApi {
    private final TaskService taskService;

    @PostMapping("/create-task")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Создает новую задачу", description = "Создает новую задачу на основании предоставленных данных в запросе.")
    public SimpleResponse createTask(@RequestBody TaskRequest request, @RequestParam Priority priority) {
        /**
         * Создает новую задачу на основании предоставленных данных в запросе.
         * @param request - объект, содержащий данные для создания задачи.
         * @param priority - время выполнения.
         * @return SimpleResponse - ответ с информацией о результате операции.
         */
        return taskService.createTask(request, priority);
    }

    @GetMapping("/get-task-by/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Получает задачу по ID", description = "Получает задачу по уникальному идентификатору.")
    public TaskResponse getTaskById(@PathVariable Long id) {
        /**
         * Получает задачу по ID.
         * @param id - уникальный идентификатор задачи.
         * @return TaskResponse - объект, содержащий информацию о найденной задаче.
         */
        return taskService.getTaskById(id);
    }

    @GetMapping("/get-all-tasks")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Получает список всех задач", description = "Возвращает список всех задач.")
    public List<TaskResponsePagination> getAllTasks() {
        /**
         * Получает список всех задач.
         * @return List<TaskResponsePagination> - список объектов, представляющих задачи.
         */
        return taskService.getAllTasks();
    }

    @PutMapping("/update-task/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Обновляет существующую задачу", description = "Обновляет существующую задачу на основании предоставленных данных.")
    public SimpleResponse updateTask(@PathVariable Long id, @RequestBody TaskRequest request, @RequestParam Priority priority) {
        /**
         * Обновляет существующую задачу на основании предоставленных данных.
         * @param id - уникальный идентификатор задачи, которую нужно обновить.
         * @param request - объект, содержащий новые данные для задачи.
         * @param priority - время выполнения.
         * @return SimpleResponse - ответ с информацией о результате операции.
         */
        return taskService.updateTask(id, request, priority);
    }

    @DeleteMapping("/delete-task/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Удаляет задачу по ID", description = "Удаляет задачу по уникальному идентификатору.")
    public SimpleResponse deleteTask(@PathVariable Long id) {
        /**
         * Удаляет задачу по ID.
         * @param id - уникальный идентификатор задачи, которую нужно удалить.
         * @return SimpleResponse - ответ с информацией о результате операции.
         */
        return taskService.deleteTask(id);
    }

    @PostMapping("/{taskId}/assign")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Назначает исполнителя для задачи", description = "Назначает исполнителя для задачи.")
    public SimpleResponse assignAssignee(@PathVariable Long taskId, @RequestParam String emailMember) {
        /**
         * Назначает исполнителя для задачи.
         * @param taskId - уникальный идентификатор задачи, к которой назначается исполнитель.
         * @param assigneeId - уникальный идентификатор исполнителя.
         * @return SimpleResponse - ответ с информацией о результате операции.
         */
        return taskService.assignAssignee(taskId, emailMember);
    }

    @PutMapping("/{taskId}/change-status")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Изменяет статус задачи", description = "Изменяет статус задачи.")
    public SimpleResponse changeStatus(@PathVariable Long taskId, @RequestParam Status status) {
        /**
         * Изменяет статус задачи.
         * @param taskId - уникальный идентификатор задачи, статус которой нужно изменить.
         * @param status - новый статус, который нужно установить.
         * @return SimpleResponse - ответ с информацией о результате операции.
         */
        return taskService.changeStatus(taskId, status);
    }

    @GetMapping("/api/tasks/creator/{creatorId}")
    public Page<TaskResponsePagination> getTasksByCreator(@PathVariable Long creatorId,
                                                          @RequestParam Status status,
                                                          @RequestParam int currentPage,
                                                          @RequestParam int pageSize) {
        /**
         * Пагинация страницы
         * @param creatorId - айди создателя.
         * @param status - статус задания.
         * @param currentPage - номер страницы.
         * @param pageSize - количество данных на странице.
         * @request TaskResponsePagination - ответ с пагинацией.
         */
        return taskService.getTasksByCreator(creatorId, status, currentPage, pageSize);
    }
}