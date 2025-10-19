package com.example.tasksapi.service;

import com.example.tasksapi.api.model.*;
import com.example.tasksapi.exception.BadRequestException;
import com.example.tasksapi.exception.ConflictException;
import com.example.tasksapi.exception.NotFoundException;
import com.example.tasksapi.persistence.entity.TaskEntity;
import com.example.tasksapi.persistence.entity.TaskStatus;
import com.example.tasksapi.persistence.entity.UserEntity;
import com.example.tasksapi.persistence.repository.TaskRepository;
import com.example.tasksapi.persistence.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public ListTasksResponse listTasks(
            TaskStatus status,
            String group,
            String ownerId,
            Boolean unassigned,
            String q,
            int page,
            int pageSize) {

        if (unassigned != null && unassigned && ownerId != null) {
            throw new BadRequestException("Cannot combine unassigned=true with ownerId filter");
        }
        if (page < 1) throw new BadRequestException("page must be >= 1");
        if (pageSize < 1 || pageSize > 200) throw new BadRequestException("pageSize must be between 1 and 200");

        Specification<TaskEntity> spec = Specification.where(null);
        if (status != null) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("status"), status));
        }
        if (group != null) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("groupId"), group));
        }
        if (ownerId != null) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("owner").get("id"), ownerId));
        }
        if (unassigned != null && unassigned) {
            spec = spec.and((root, cq, cb) -> cb.isNull(root.get("owner")));
        }
        if (q != null && !q.isBlank()) {
            String pattern = "%" + q.toLowerCase() + "%";
            spec = spec.and((root, cq, cb) -> cb.like(cb.lower(root.get("title")), pattern));
        }

        PageRequest pr = PageRequest.of(page - 1, pageSize);
        Page<TaskEntity> p = taskRepository.findAll(spec, pr);

        List<Task> items = p.getContent().stream().map(TaskMapper::toDto).toList();

        ListTasksResponse resp = new ListTasksResponse();
        resp.setItems(items);
        resp.setPage(page);
        resp.setPageSize(pageSize);
        resp.setTotal(p.getTotalElements());
        resp.setHasNext(p.hasNext());
        return resp;
    }

    @Transactional
    public Task createTask(CreateTaskRequest req) {
        TaskEntity e = new TaskEntity();
        e.setTitle(req.getTitle());
        e.setGroupId(req.getGroup());
        TaskStatus status = Optional.ofNullable(req.getStatus())
                .map(s -> TaskStatus.valueOf(s.name()))
                .orElse(TaskStatus.todo);
        e.setStatus(status);

        if (req.getOwnerId() != null) {
            UserEntity owner = userRepository.findById(req.getOwnerId())
                    .orElseThrow(() -> new BadRequestException("ownerId not found: " + req.getOwnerId()));
            e.setOwner(owner);
        }
        TaskEntity saved = taskRepository.save(e);
        return TaskMapper.toDto(saved);
    }

    public Task getTask(String id) {
        TaskEntity e = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found: " + id));
        return TaskMapper.toDto(e);
    }

    @Transactional
    public Task updateTask(String id,
                           String title,
                           com.example.tasksapi.persistence.entity.TaskStatus status,
                           boolean groupPresent,
                           String group,
                           boolean ownerIdPresent,
                           String ownerId) {
        TaskEntity e = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found: " + id));

        if (title != null) e.setTitle(title);
        if (status != null) e.setStatus(status);
        if (groupPresent) {
            e.setGroupId(group); // may be null to clear
        }
        if (ownerIdPresent) {
            if (ownerId != null) {
                UserEntity owner = userRepository.findById(ownerId)
                        .orElseThrow(() -> new BadRequestException("ownerId not found: " + ownerId));
                e.setOwner(owner);
            } else {
                e.setOwner(null); // explicit unassign
            }
        }

        return TaskMapper.toDto(e);
    }

    @Transactional
    public void deleteTask(String id) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task not found: " + id);
        }
        taskRepository.deleteById(id);
    }

    @Transactional
    public Task lockTask(String id, String userId) {
        if (userId == null || userId.isBlank()) {
            throw new BadRequestException("userId is required to lock without auth context");
        }
        TaskEntity e = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found: " + id));
        if (e.getOwner() != null) {
            throw new ConflictException("Task is already assigned");
        }
        UserEntity owner = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("userId not found: " + userId));
        e.setOwner(owner);
        return TaskMapper.toDto(e);
    }

    @Transactional
    public Task unlockTask(String id, String userId) {
        TaskEntity e = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found: " + id));
        if (e.getOwner() == null) {
            throw new ConflictException("Task already unassigned");
        }
        // Policy: allow any user (no auth). userId ignored if provided.
        e.setOwner(null);
        return TaskMapper.toDto(e);
    }
}
