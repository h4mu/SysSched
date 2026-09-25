package com.example.systemscheduler.operation;

import android.content.Context;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;
import com.example.systemscheduler.model.OperationType;

public class OperationDispatcher {

    public static SystemOperation getOperationHandler(OperationType type) {
        if (type == null) return null;
        switch (type) {
            case WIFI:
                return new WifiOperation();
            case BLUETOOTH:
                return new BluetoothOperation();
            case MOBILE_DATA:
                return new MobileDataOperation();
            case SYNC:
                return new SyncOperation();
            case LOCATION:
                return new LocationOperation();
            case NFC:
                return new NfcOperation();
            case POWER_SAVING:
                return new PowerSavingOperation();
            case REBOOT:
                return new RebootOperation();
            case SHUTDOWN:
                return new ShutdownOperation();
            default:
                return null;
        }
    }

    public static CapabilityLevel getCapability(Context context, OperationType type) {
        SystemOperation handler = getOperationHandler(type);
        if (handler != null) {
            return handler.getCapability(context);
        }
        return CapabilityLevel.UNSUPPORTED;
    }

    public static ExecutionResultStatus dispatch(Context context, OperationType type, ActionType action) {
        SystemOperation handler = getOperationHandler(type);
        if (handler != null) {
            return handler.execute(context, action);
        }
        return ExecutionResultStatus.NOT_SUPPORTED;
    }
}
