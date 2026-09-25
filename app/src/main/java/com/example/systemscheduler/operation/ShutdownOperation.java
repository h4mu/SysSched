package com.example.systemscheduler.operation;

import android.content.Context;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

public class ShutdownOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        return CapabilityLevel.PRIVILEGED;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        // Consumer third-party apps cannot shut down device directly without system/root privileges
        return ExecutionResultStatus.PERMISSION_DENIED;
    }
}
