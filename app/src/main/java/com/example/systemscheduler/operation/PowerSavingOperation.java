package com.example.systemscheduler.operation;

import android.content.Context;
import android.provider.Settings;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

public class PowerSavingOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        return CapabilityLevel.DIRECT;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        boolean enable = (action == ActionType.ON);
        try {
            boolean success = Settings.Global.putInt(
                    context.getContentResolver(),
                    "low_power",
                    enable ? 1 : 0
            );
            return success ? ExecutionResultStatus.SUCCESS : ExecutionResultStatus.FAILED;
        } catch (SecurityException e) {
            return ExecutionResultStatus.PERMISSION_DENIED;
        } catch (Exception e) {
            return ExecutionResultStatus.FAILED;
        }
    }
}
