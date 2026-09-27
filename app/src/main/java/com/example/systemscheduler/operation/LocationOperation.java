package com.example.systemscheduler.operation;

import android.content.Context;
import android.provider.Settings;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

public class LocationOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        return CapabilityLevel.DIRECT;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        int mode = (action == ActionType.ON)
                ? Settings.Secure.LOCATION_MODE_HIGH_ACCURACY
                : Settings.Secure.LOCATION_MODE_OFF;
        try {
            boolean success = Settings.Secure.putInt(
                    context.getContentResolver(),
                    Settings.Secure.LOCATION_MODE,
                    mode
            );
            return success ? ExecutionResultStatus.SUCCESS : ExecutionResultStatus.FAILED;
        } catch (SecurityException e) {
            return ExecutionResultStatus.PERMISSION_DENIED;
        } catch (Exception e) {
            return ExecutionResultStatus.FAILED;
        }
    }
}
