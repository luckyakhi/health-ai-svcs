package com.example.tasksapi.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}

