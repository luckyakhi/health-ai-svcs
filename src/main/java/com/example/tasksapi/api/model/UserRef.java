package com.example.tasksapi.api.model;

import jakarta.validation.constraints.NotBlank;

public class UserRef {
    @NotBlank
    private String id;

    @NotBlank
    private String name;

    public UserRef() {}

    public UserRef(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

