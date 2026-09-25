package com.example.systemscheduler.model;

import android.content.Context;
import com.example.systemscheduler.R;

public enum ActionType {
    ON(R.string.action_on, "ON"),
    OFF(R.string.action_off, "OFF");

    private final int stringResId;
    private final String fallbackDisplayName;

    ActionType(int stringResId, String fallbackDisplayName) {
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
