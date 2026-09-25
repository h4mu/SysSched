package com.example.systemscheduler.model;

public enum CapabilityLevel {
    DIRECT("Direct control permitted"),
    USER_ACTION_REQUIRED("User action required / Open settings"),
    PRIVILEGED("Requires elevated permissions/root/system app"),
    UNSUPPORTED("Unsupported on this hardware/Android version");

    private final String description;

    CapabilityLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
