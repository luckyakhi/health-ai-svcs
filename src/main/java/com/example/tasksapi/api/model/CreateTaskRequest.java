package com.example.tasksapi.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = false)
public class CreateTaskRequest {
    @NotBlank
    private String title;

    private TaskStatus status; // optional; default todo

    private String group; // nullable

    private String ownerId; // nullable

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
}

