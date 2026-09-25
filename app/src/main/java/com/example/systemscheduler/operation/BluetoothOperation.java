package com.example.systemscheduler.operation;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

public class BluetoothOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ restricts enable/disable directly for non-system apps
            return CapabilityLevel.USER_ACTION_REQUIRED;
        }
        return CapabilityLevel.DIRECT;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            return ExecutionResultStatus.NOT_SUPPORTED;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return ExecutionResultStatus.USER_ACTION_REQUIRED;
        } else {
            try {
                boolean success;
                if (action == ActionType.ON) {
                    @SuppressWarnings("deprecation")
                    boolean res = adapter.enable();
                    success = res;
                } else {
                    @SuppressWarnings("deprecation")
                    boolean res = adapter.disable();
                    success = res;
                }
                return success ? ExecutionResultStatus.SUCCESS : ExecutionResultStatus.FAILED;
            } catch (SecurityException e) {
                return ExecutionResultStatus.PERMISSION_DENIED;
            }
        }
    }
}
