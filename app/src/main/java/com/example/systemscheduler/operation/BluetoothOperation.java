package com.example.systemscheduler.operation;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

public class BluetoothOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        return CapabilityLevel.DIRECT;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            return ExecutionResultStatus.NOT_SUPPORTED;
        }

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
        } catch (Exception e) {
            return ExecutionResultStatus.FAILED;
        }
    }
}
