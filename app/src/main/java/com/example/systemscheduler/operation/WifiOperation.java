package com.example.systemscheduler.operation;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

public class WifiOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ (API 29+) restricts setWifiEnabled for standard apps
            return CapabilityLevel.USER_ACTION_REQUIRED;
        }
        return CapabilityLevel.DIRECT;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return ExecutionResultStatus.USER_ACTION_REQUIRED;
        } else {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                boolean enabled = (action == ActionType.ON);
                try {
                    @SuppressWarnings("deprecation")
                    boolean success = wifiManager.setWifiEnabled(enabled);
                    return success ? ExecutionResultStatus.SUCCESS : ExecutionResultStatus.FAILED;
                } catch (SecurityException e) {
                    return ExecutionResultStatus.PERMISSION_DENIED;
                }
            }
            return ExecutionResultStatus.FAILED;
        }
    }
}
