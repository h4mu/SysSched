package com.example.systemscheduler.operation;

import android.content.Context;
import android.os.PowerManager;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

public class RebootOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        return CapabilityLevel.PRIVILEGED;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        // Standard third-party apps cannot trigger system reboot without root/privileged system permissions.
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            try {
                pm.reboot(null);
                return ExecutionResultStatus.SUCCESS;
            } catch (SecurityException e) {
                return ExecutionResultStatus.PERMISSION_DENIED;
            }
        }
        return ExecutionResultStatus.FAILED;
    }
}
