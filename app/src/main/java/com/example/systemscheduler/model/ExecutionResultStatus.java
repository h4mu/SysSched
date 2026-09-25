package com.example.systemscheduler.model;

public enum ExecutionResultStatus {
    SUCCESS("Operation succeeded"),
    USER_ACTION_REQUIRED("User action required (Settings opened)"),
    PERMISSION_DENIED("Permission denied"),
    NOT_SUPPORTED("Operation not supported"),
    FAILED("Operation failed");

    private final String message;

    ExecutionResultStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
