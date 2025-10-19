package com.example.tasksapi.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Task {
    @NotBlank
    private String id;

    @NotBlank
    private String title;

    @NotNull
    private TaskStatus status;

    private String group;

    private UserRef owner; // nullable

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public UserRef getOwner() { return owner; }
    public void setOwner(UserRef owner) { this.owner = owner; }
}

