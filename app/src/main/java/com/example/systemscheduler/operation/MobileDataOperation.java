package com.example.systemscheduler.operation;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.provider.Settings;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

import java.lang.reflect.Method;

public class MobileDataOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        return CapabilityLevel.DIRECT;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        boolean enabled = (action == ActionType.ON);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (tm != null) {
            try {
                Method setDataEnabled = tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
                setDataEnabled.setAccessible(true);
                setDataEnabled.invoke(tm, enabled);
                return ExecutionResultStatus.SUCCESS;
            } catch (SecurityException e) {
                return ExecutionResultStatus.PERMISSION_DENIED;
            } catch (Exception ignored) {
            }
        }

        try {
            boolean success = Settings.Global.putInt(
                    context.getContentResolver(),
                    "mobile_data",
                    enabled ? 1 : 0
            );
            return success ? ExecutionResultStatus.SUCCESS : ExecutionResultStatus.FAILED;
        } catch (SecurityException e) {
            return ExecutionResultStatus.PERMISSION_DENIED;
        } catch (Exception e) {
            return ExecutionResultStatus.FAILED;
        }
    }
}
