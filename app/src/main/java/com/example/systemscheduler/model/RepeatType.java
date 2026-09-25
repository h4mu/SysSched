package com.example.systemscheduler.model;

import android.content.Context;
import com.example.systemscheduler.R;

public enum RepeatType {
    ONCE(R.string.repeat_once, "One-time"),
    DAILY(R.string.repeat_daily, "Every day"),
    WEEKLY(R.string.repeat_weekly, "Weekly");

    private final int stringResId;
    private final String fallbackDisplayName;

    RepeatType(int stringResId, String fallbackDisplayName) {
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
