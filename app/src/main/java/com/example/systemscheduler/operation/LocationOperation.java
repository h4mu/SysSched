package com.example.systemscheduler.operation;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

public class LocationOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        // Modern Android strictly restricts third-party apps from changing global Location setting silently
        return CapabilityLevel.USER_ACTION_REQUIRED;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return ExecutionResultStatus.USER_ACTION_REQUIRED;
    }
}
