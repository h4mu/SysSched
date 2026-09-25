package com.example.systemscheduler.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.systemscheduler.R;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.ExecutionResultStatus;
import com.example.systemscheduler.model.Schedule;
import com.example.systemscheduler.operation.OperationDispatcher;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    public interface OnScheduleClickListener {
        void onScheduleClick(Schedule schedule);
        void onScheduleToggle(Schedule schedule, boolean isChecked);
        void onScheduleLongClick(Schedule schedule);
    }

    private final List<Schedule> schedules = new ArrayList<>();
    private final OnScheduleClickListener listener;

    public ScheduleAdapter(OnScheduleClickListener listener) {
        this.listener = listener;
    }

    public void setSchedules(List<Schedule> newSchedules) {
        schedules.clear();
        if (newSchedules != null) {
            schedules.addAll(newSchedules);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule schedule = schedules.get(position);
        holder.bind(schedule, listener);
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textTime;
        final TextView textOperation;
        final TextView textRepeat;
        final SwitchMaterial switchEnabled;
        final TextView textCapabilityWarning;
        final TextView textLastExecution;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTime = itemView.findViewById(R.id.text_time);
            textOperation = itemView.findViewById(R.id.text_operation);
            textRepeat = itemView.findViewById(R.id.text_repeat);
            switchEnabled = itemView.findViewById(R.id.switch_enabled);
            textCapabilityWarning = itemView.findViewById(R.id.text_capability_warning);
            textLastExecution = itemView.findViewById(R.id.text_last_execution);
        }

        void bind(final Schedule schedule, final OnScheduleClickListener listener) {
            Context context = itemView.getContext();

            textTime.setText(String.format(Locale.getDefault(), "%02d:%02d", schedule.getHour(), schedule.getMinute()));

            String opName = schedule.getOperation() != null ? schedule.getOperation().getDisplayName(context) : "";
            String actName = schedule.getAction() != null ? schedule.getAction().getDisplayName(context) : "";
            textOperation.setText(String.format("%s — %s", opName, actName));

            textRepeat.setText(schedule.getRepeatType() != null ? schedule.getRepeatType().getDisplayName(context) : "");

            switchEnabled.setOnCheckedChangeListener(null);
            switchEnabled.setChecked(schedule.isEnabled());
            switchEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) {
                    listener.onScheduleToggle(schedule, isChecked);
                }
            });

            CapabilityLevel capability = OperationDispatcher.getCapability(context, schedule.getOperation());
            if (capability == CapabilityLevel.USER_ACTION_REQUIRED) {
                textCapabilityWarning.setVisibility(View.VISIBLE);
                textCapabilityWarning.setText(R.string.capability_user_action_required);
            } else if (capability == CapabilityLevel.PRIVILEGED) {
                textCapabilityWarning.setVisibility(View.VISIBLE);
                textCapabilityWarning.setText(R.string.capability_privileged);
            } else if (capability == CapabilityLevel.UNSUPPORTED) {
                textCapabilityWarning.setVisibility(View.VISIBLE);
                textCapabilityWarning.setText(R.string.capability_unsupported);
            } else {
                textCapabilityWarning.setVisibility(View.GONE);
            }

            if (schedule.getLastExecution() > 0 && schedule.getLastResultStatus() != null) {
                textLastExecution.setVisibility(View.VISIBLE);
                String statusStr = schedule.getLastResultStatus();
                try {
                    ExecutionResultStatus statusEnum = ExecutionResultStatus.valueOf(statusStr);
                    statusStr = statusEnum.getMessage(context);
                } catch (Exception ignored) {
                }
                textLastExecution.setText(context.getString(R.string.last_status_format, statusStr));
            } else {
                textLastExecution.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onScheduleClick(schedule);
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) listener.onScheduleLongClick(schedule);
                return true;
            });
        }
    }
}
