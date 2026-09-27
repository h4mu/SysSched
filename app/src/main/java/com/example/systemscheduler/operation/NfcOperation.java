package com.example.systemscheduler.operation;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.provider.Settings;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

import java.lang.reflect.Method;

public class NfcOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
        if (adapter == null) {
            return CapabilityLevel.UNSUPPORTED;
        }
        return CapabilityLevel.DIRECT;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
        if (adapter == null) {
            return ExecutionResultStatus.NOT_SUPPORTED;
        }

        boolean enable = (action == ActionType.ON);

        try {
            Method method = enable
                    ? adapter.getClass().getDeclaredMethod("enable")
                    : adapter.getClass().getDeclaredMethod("disable");
            method.setAccessible(true);
            boolean success = (Boolean) method.invoke(adapter);
            if (success) {
                return ExecutionResultStatus.SUCCESS;
            }
        } catch (SecurityException e) {
            return ExecutionResultStatus.PERMISSION_DENIED;
        } catch (Exception ignored) {
        }

        try {
            boolean success = Settings.Global.putInt(
                    context.getContentResolver(),
                    "nfc_on",
                    enable ? 1 : 0
            );
            return success ? ExecutionResultStatus.SUCCESS : ExecutionResultStatus.FAILED;
        } catch (SecurityException e) {
            return ExecutionResultStatus.PERMISSION_DENIED;
        } catch (Exception e) {
            return ExecutionResultStatus.FAILED;
        }
    }
}
