package com.example.systemscheduler.operation;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

public class PowerSavingOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        // Direct modification of Power Saving mode requires WRITE_SECURE_SETTINGS or privileged app permissions
        return CapabilityLevel.USER_ACTION_REQUIRED;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        Intent intent = new Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS);
        if (intent.resolveActivity(context.getPackageManager()) == null) {
            intent = new Intent(Settings.ACTION_SETTINGS);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return ExecutionResultStatus.USER_ACTION_REQUIRED;
    }
}
