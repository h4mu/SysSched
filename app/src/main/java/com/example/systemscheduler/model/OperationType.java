package com.example.systemscheduler.model;

import android.content.Context;
import com.example.systemscheduler.R;

public enum OperationType {
    WIFI(R.string.op_wifi, "Wi-Fi"),
    BLUETOOTH(R.string.op_bluetooth, "Bluetooth"),
    MOBILE_DATA(R.string.op_mobile_data, "Mobile Data"),
    SYNC(R.string.op_sync, "Automatic Sync"),
    LOCATION(R.string.op_location, "Location / GPS"),
    NFC(R.string.op_nfc, "NFC"),
    POWER_SAVING(R.string.op_power_saving, "Power Saving"),
    REBOOT(R.string.op_reboot, "Device Reboot"),
    SHUTDOWN(R.string.op_shutdown, "Device Shutdown");

    private final int stringResId;
    private final String fallbackDisplayName;

    OperationType(int stringResId, String fallbackDisplayName) {
        this.stringResId = stringResId;
        this.fallbackDisplayName = fallbackDisplayName;
    }

    public String getDisplayName(Context context) {
        if (context != null) {
            return context.getString(stringResId);
        }
        return fallbackDisplayName;
    }

    public String getDisplayName() {
        return fallbackDisplayName;
    }
}
