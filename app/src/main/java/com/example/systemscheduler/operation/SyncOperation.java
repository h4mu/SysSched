package com.example.systemscheduler.operation;

import android.content.ContentResolver;
import android.content.Context;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

public class SyncOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        return CapabilityLevel.DIRECT;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        try {
            boolean enable = (action == ActionType.ON);
            ContentResolver.setMasterSyncAutomatically(enable);
            return ExecutionResultStatus.SUCCESS;
        } catch (SecurityException e) {
            return ExecutionResultStatus.PERMISSION_DENIED;
        } catch (Exception e) {
            return ExecutionResultStatus.FAILED;
        }
    }
}
