package com.example.systemscheduler.operation;

import android.content.Context;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

public interface SystemOperation {
    CapabilityLevel getCapability(Context context);
    ExecutionResultStatus execute(Context context, ActionType action);
}
