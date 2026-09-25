package com.example.systemscheduler.model;

public enum ActionType {
    ON("ON"),
    OFF("OFF");

    private final String displayName;

    ActionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
