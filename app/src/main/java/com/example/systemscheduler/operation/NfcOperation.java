package com.example.systemscheduler.operation;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.provider.Settings;

import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;

public class NfcOperation implements SystemOperation {

    @Override
    public CapabilityLevel getCapability(Context context) {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
        if (adapter == null) {
            return CapabilityLevel.UNSUPPORTED;
        }
        // Direct toggling of NFC is restricted on modern Android for standard apps
        return CapabilityLevel.USER_ACTION_REQUIRED;
    }

    @Override
    public ExecutionResultStatus execute(Context context, ActionType action) {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
        if (adapter == null) {
            return ExecutionResultStatus.NOT_SUPPORTED;
        }

        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        if (intent.resolveActivity(context.getPackageManager()) == null) {
            intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return ExecutionResultStatus.USER_ACTION_REQUIRED;
    }
}
