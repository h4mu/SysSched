package com.example.systemscheduler.operation;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

import java.lang.reflect.Method;

public class ShutdownOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        return CapabilityLevel.DIRECT;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            try {
                Method shutdownMethod = pm.getClass().getMethod("shutdown", boolean.class, String.class, boolean.class);
                shutdownMethod.setAccessible(true);
                shutdownMethod.invoke(pm, false, null, false);
                return ExecutionResultStatus.SUCCESS;
            } catch (SecurityException e) {
                return ExecutionResultStatus.PERMISSION_DENIED;
            } catch (Exception ignored) {
            }
        }

        try {
            Intent intent = new Intent("com.android.internal.intent.action.REQUEST_SHUTDOWN");
            intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return ExecutionResultStatus.SUCCESS;
        } catch (SecurityException e) {
            return ExecutionResultStatus.PERMISSION_DENIED;
        } catch (Exception e) {
            return ExecutionResultStatus.FAILED;
        }
    }
}
