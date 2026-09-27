package com.example.systemscheduler.operation;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.PowerManager;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;
import com.example.systemscheduler.receiver.AdminReceiver;

public class RebootOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        return CapabilityLevel.DIRECT;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminName = new ComponentName(context, AdminReceiver.class);

        if (dpm != null && dpm.isDeviceOwnerApp(context.getPackageName())) {
            try {
                dpm.reboot(adminName);
                return ExecutionResultStatus.SUCCESS;
            } catch (SecurityException e) {
                return ExecutionResultStatus.PERMISSION_DENIED;
            } catch (Exception ignored) {
            }
        }

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            try {
                pm.reboot(null);
                return ExecutionResultStatus.SUCCESS;
            } catch (SecurityException e) {
                return ExecutionResultStatus.PERMISSION_DENIED;
            } catch (Exception e) {
                return ExecutionResultStatus.FAILED;
            }
        }
        return ExecutionResultStatus.FAILED;
    }
}
