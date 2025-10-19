package com.example.tasksapi.api;

import com.example.tasksapi.api.model.*;
import com.example.tasksapi.exception.BadRequestException;
import com.example.tasksapi.service.TaskService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ObjectMapper objectMapper;

    public TaskController(TaskService taskService, ObjectMapper objectMapper) {
        this.taskService = taskService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ListTasksResponse list(
            @RequestParam(value = "status", required = false) TaskStatus status,
            @RequestParam(value = "group", required = false) String group,
            @RequestParam(value = "ownerId", required = false) String ownerId,
            @RequestParam(value = "unassigned", required = false) Boolean unassigned,
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "25") int pageSize
    ) {
        com.example.tasksapi.persistence.entity.TaskStatus st = null;
        if (status != null) {
            st = com.example.tasksapi.persistence.entity.TaskStatus.valueOf(status.name());
        }
        return taskService.listTasks(st, group, ownerId, unassigned, q, page, pageSize);
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody CreateTaskRequest req) {
        Task created = taskService.createTask(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public Task get(@PathVariable("id") String id) {
        return taskService.getTask(id);
    }

    @PatchMapping("/{id}")
    public Task patch(@PathVariable("id") String id, @RequestBody JsonNode node) {
        // Validate payload against UpdateTaskRequest for unknown fields/shape
        try {
            UpdateTaskRequest req = objectMapper.treeToValue(node, UpdateTaskRequest.class);
            String title = req.getTitle();
            com.example.tasksapi.persistence.entity.TaskStatus st = null;
            if (req.getStatus() != null) {
                st = com.example.tasksapi.persistence.entity.TaskStatus.valueOf(req.getStatus().name());
            }
            boolean groupPresent = node.has("group");
            String group = groupPresent ? (req.getGroup()) : null;
            boolean ownerPresent = node.has("ownerId");
            String ownerId = ownerPresent ? req.getOwnerId() : null; // may be null to unassign
            return taskService.updateTask(id, title, st, groupPresent, group, ownerPresent, ownerId);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid enum or payload: " + e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Invalid payload: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        taskService.deleteTask(id);
    }

    @PostMapping("/{id}/lock")
    public Task lock(@PathVariable("id") String id, @RequestBody(required = false) LockRequest req) {
        String userId = req != null ? req.getUserId() : null;
        return taskService.lockTask(id, userId);
    }

    @PostMapping("/{id}/unlock")
    public Task unlock(@PathVariable("id") String id, @RequestBody(required = false) LockRequest req) {
        String userId = req != null ? req.getUserId() : null;
        return taskService.unlockTask(id, userId);
    }
}
