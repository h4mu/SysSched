package com.example.systemscheduler.model;

public enum OperationType {
    WIFI("Wi-Fi"),
    BLUETOOTH("Bluetooth"),
    MOBILE_DATA("Mobile Data"),
    SYNC("Automatic Sync"),
    LOCATION("Location / GPS"),
    NFC("NFC"),
    POWER_SAVING("Power Saving"),
    REBOOT("Device Reboot"),
    SHUTDOWN("Device Shutdown");

    private final String displayName;

    OperationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
