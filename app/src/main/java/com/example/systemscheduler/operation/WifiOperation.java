package com.example.systemscheduler.operation;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

public class WifiOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        return CapabilityLevel.DIRECT;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        boolean enabled = (action == ActionType.ON);
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager != null) {
            try {
                @SuppressWarnings("deprecation")
                boolean success = wifiManager.setWifiEnabled(enabled);
                if (success) {
                    return ExecutionResultStatus.SUCCESS;
                }
            } catch (SecurityException ignored) {
            }
        }

        try {
            boolean success = Settings.Global.putInt(
                    context.getContentResolver(),
                    Settings.Global.WIFI_ON,
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
