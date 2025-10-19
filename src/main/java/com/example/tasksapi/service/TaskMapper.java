package com.example.tasksapi.service;

import com.example.tasksapi.api.model.Task;
import com.example.tasksapi.api.model.TaskStatus;
import com.example.tasksapi.api.model.UserRef;
import com.example.tasksapi.persistence.entity.TaskEntity;
import com.example.tasksapi.persistence.entity.UserEntity;

public class TaskMapper {
    public static Task toDto(TaskEntity e) {
        Task dto = new Task();
        dto.setId(e.getId());
        dto.setTitle(e.getTitle());
        dto.setStatus(TaskStatus.valueOf(e.getStatus().name()));
        dto.setGroup(e.getGroupId());
        UserEntity owner = e.getOwner();
        if (owner != null) {
            dto.setOwner(new UserRef(owner.getId(), owner.getName()));
        }
        return dto;
    }
}

