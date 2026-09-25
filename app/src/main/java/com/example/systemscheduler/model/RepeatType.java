package com.example.systemscheduler.model;

public enum RepeatType {
    ONCE("One-time"),
    DAILY("Every day"),
    WEEKLY("Weekly");

    private final String displayName;

    RepeatType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
