package com.example.systemscheduler.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.systemscheduler.data.ScheduleStore;
import com.example.systemscheduler.model.ExecutionResultStatus;
import com.example.systemscheduler.model.RepeatType;
import com.example.systemscheduler.model.Schedule;
import com.example.systemscheduler.operation.OperationDispatcher;
import com.example.systemscheduler.scheduler.AlarmScheduler;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        String scheduleId = intent.getStringExtra(AlarmScheduler.EXTRA_SCHEDULE_ID);
        if (scheduleId == null) return;

        ScheduleStore store = new ScheduleStore(context);
        Schedule schedule = store.getScheduleById(scheduleId);
        if (schedule == null || !schedule.isEnabled()) return;

        // Dispatch operation execution
        ExecutionResultStatus result = OperationDispatcher.dispatch(context, schedule.getOperation(), schedule.getAction());

        // Update schedule state
        schedule.setLastExecution(System.currentTimeMillis());
        schedule.setLastResultStatus(result.name());

        if (schedule.getRepeatType() == RepeatType.ONCE) {
            schedule.setEnabled(false);
        }

        store.saveSchedule(schedule);

        // Reschedule if recurring and enabled
        if (schedule.isEnabled()) {
            AlarmScheduler scheduler = new AlarmScheduler(context);
            scheduler.schedule(schedule);
        }
    }
}
