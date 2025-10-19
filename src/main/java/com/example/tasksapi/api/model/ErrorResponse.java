package com.example.tasksapi.api.model;

import jakarta.validation.constraints.NotBlank;

public class ErrorResponse {
    @NotBlank
    private String code;
    @NotBlank
    private String message;
    private Object details; // nullable

    public ErrorResponse() {}

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(String code, String message, Object details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getDetails() { return details; }
    public void setDetails(Object details) { this.details = details; }
}

