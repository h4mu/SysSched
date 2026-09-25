package com.example.systemscheduler;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.systemscheduler.data.ScheduleStore;
import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.OperationType;
import com.example.systemscheduler.model.RepeatType;
import com.example.systemscheduler.model.Schedule;
import com.example.systemscheduler.scheduler.AlarmScheduler;
import com.example.systemscheduler.ui.ScheduleAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ScheduleAdapter.OnScheduleClickListener {

    private ScheduleStore scheduleStore;
    private AlarmScheduler alarmScheduler;
    private ScheduleAdapter adapter;
    private TextView textEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduleStore = new ScheduleStore(this);
        alarmScheduler = new AlarmScheduler(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_schedules);
        textEmptyState = findViewById(R.id.text_empty_state);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add_schedule);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScheduleAdapter(this);
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showScheduleEditorDialog(null));

        loadSchedules();
    }

    private void loadSchedules() {
        List<Schedule> list = scheduleStore.getAllSchedules();
        if (list.isEmpty()) {
            textEmptyState.setVisibility(View.VISIBLE);
        } else {
            textEmptyState.setVisibility(View.GONE);
        }
        adapter.setSchedules(list);
    }

    @Override
    public void onScheduleClick(Schedule schedule) {
        showScheduleEditorDialog(schedule);
    }

    @Override
    public void onScheduleToggle(Schedule schedule, boolean isChecked) {
        schedule.setEnabled(isChecked);
        scheduleStore.saveSchedule(schedule);
        if (isChecked) {
            alarmScheduler.schedule(schedule);
        } else {
            alarmScheduler.cancel(schedule);
        }
        loadSchedules();
    }

    @Override
    public void onScheduleLongClick(Schedule schedule) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_message)
                .setPositiveButton(R.string.dialog_delete_confirm, (dialog, which) -> {
                    alarmScheduler.cancel(schedule);
                    scheduleStore.deleteSchedule(schedule.getId());
                    loadSchedules();
                    Toast.makeText(MainActivity.this, R.string.toast_schedule_deleted, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }

    private void showScheduleEditorDialog(final Schedule existingSchedule) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_schedule_editor, null);
        builder.setView(dialogView);

        TextView title = dialogView.findViewById(R.id.dialog_title);
        Spinner spinnerOp = dialogView.findViewById(R.id.spinner_operation);
        Spinner spinnerAct = dialogView.findViewById(R.id.spinner_action);
        TimePicker timePicker = dialogView.findViewById(R.id.time_picker);
        Spinner spinnerRepeat = dialogView.findViewById(R.id.spinner_repeat);

        timePicker.setIs24HourView(true);

        if (existingSchedule != null) {
            title.setText(R.string.dialog_title_edit);
        } else {
            title.setText(R.string.dialog_title_new);
        }

        // Setup Spinners
        List<String> opDisplayNames = new ArrayList<>();
        for (OperationType op : OperationType.values()) {
            opDisplayNames.add(op.getDisplayName(this));
        }
        ArrayAdapter<String> opAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opDisplayNames);
        opAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOp.setAdapter(opAdapter);

        List<String> actDisplayNames = new ArrayList<>();
        for (ActionType act : ActionType.values()) {
            actDisplayNames.add(act.getDisplayName(this));
        }
        ArrayAdapter<String> actAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, actDisplayNames);
        actAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAct.setAdapter(actAdapter);

        List<String> repeatDisplayNames = new ArrayList<>();
        for (RepeatType rep : RepeatType.values()) {
            repeatDisplayNames.add(rep.getDisplayName(this));
        }
        ArrayAdapter<String> repeatAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, repeatDisplayNames);
        repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepeat.setAdapter(repeatAdapter);

        // Populate initial selection
        if (existingSchedule != null) {
            spinnerOp.setSelection(existingSchedule.getOperation().ordinal());
            spinnerAct.setSelection(existingSchedule.getAction().ordinal());
            spinnerRepeat.setSelection(existingSchedule.getRepeatType().ordinal());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.setHour(existingSchedule.getHour());
                timePicker.setMinute(existingSchedule.getMinute());
            } else {
                timePicker.setCurrentHour(existingSchedule.getHour());
                timePicker.setCurrentMinute(existingSchedule.getMinute());
            }
        }

        builder.setPositiveButton(R.string.dialog_save, (dialog, which) -> {
            OperationType op = OperationType.values()[spinnerOp.getSelectedItemPosition()];
            ActionType act = ActionType.values()[spinnerAct.getSelectedItemPosition()];
            RepeatType repeat = RepeatType.values()[spinnerRepeat.getSelectedItemPosition()];

            int hour = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? timePicker.getHour() : timePicker.getCurrentHour();
            int minute = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? timePicker.getMinute() : timePicker.getCurrentMinute();

            Schedule schedule = existingSchedule != null ? existingSchedule : new Schedule();
            schedule.setOperation(op);
            schedule.setAction(act);
            schedule.setHour(hour);
            schedule.setMinute(minute);
            schedule.setRepeatType(repeat);
            schedule.setEnabled(true);

            scheduleStore.saveSchedule(schedule);
            alarmScheduler.schedule(schedule);

            loadSchedules();
            Toast.makeText(MainActivity.this, R.string.toast_schedule_saved, Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton(R.string.dialog_cancel, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
