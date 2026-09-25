package com.example.systemscheduler.model;

import android.content.Context;
import com.example.systemscheduler.R;

public enum ExecutionResultStatus {
    SUCCESS(R.string.status_success, "Operation succeeded"),
    USER_ACTION_REQUIRED(R.string.status_user_action_required, "User action required (Settings opened)"),
    PERMISSION_DENIED(R.string.status_permission_denied, "Permission denied"),
    NOT_SUPPORTED(R.string.status_not_supported, "Operation not supported"),
    FAILED(R.string.status_failed, "Operation failed");

    private final int stringResId;
    private final String fallbackMessage;

    ExecutionResultStatus(int stringResId, String fallbackMessage) {
        this.stringResId = stringResId;
        this.fallbackMessage = fallbackMessage;
    }

    public String getMessage(Context context) {
        if (context != null) {
            return context.getString(stringResId);
        }
        return fallbackMessage;
    }

    public String getMessage() {
        return fallbackMessage;
    }
}
