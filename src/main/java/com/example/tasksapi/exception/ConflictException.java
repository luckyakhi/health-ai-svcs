package com.example.tasksapi.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
}

