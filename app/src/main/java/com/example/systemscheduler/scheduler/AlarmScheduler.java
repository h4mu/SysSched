package com.example.systemscheduler.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.systemscheduler.data.ScheduleStore;
import com.example.systemscheduler.model.Schedule;
import com.example.systemscheduler.receiver.AlarmReceiver;

import java.util.List;

public class AlarmScheduler {

    public static final String EXTRA_SCHEDULE_ID = "extra_schedule_id";

    private final Context context;
    private final AlarmManager alarmManager;

    public AlarmScheduler(Context context) {
        this.context = context.getApplicationContext();
        this.alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
    }

    public void schedule(Schedule schedule) {
        if (schedule == null || !schedule.isEnabled()) {
            if (schedule != null) {
                cancel(schedule);
            }
            return;
        }

        long triggerAtMillis = schedule.getNextTriggerTime(System.currentTimeMillis());
        PendingIntent pendingIntent = getPendingIntent(schedule);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        }
    }

    public void cancel(Schedule schedule) {
        if (schedule == null || alarmManager == null) return;
        PendingIntent pendingIntent = getPendingIntent(schedule);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public void rescheduleAll() {
        ScheduleStore store = new ScheduleStore(context);
        List<Schedule> schedules = store.getAllSchedules();
        for (Schedule schedule : schedules) {
            if (schedule.isEnabled()) {
                schedule(schedule);
            } else {
                cancel(schedule);
            }
        }
    }

    private PendingIntent getPendingIntent(Schedule schedule) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_SCHEDULE_ID, schedule.getId());
        int requestCode = schedule.getId().hashCode();
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        return PendingIntent.getBroadcast(context, requestCode, intent, flags);
    }
}
