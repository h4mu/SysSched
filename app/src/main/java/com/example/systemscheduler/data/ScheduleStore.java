package com.example.systemscheduler.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.systemscheduler.model.Schedule;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ScheduleStore {
    private static final String PREF_NAME = "system_scheduler_prefs";
    private static final String KEY_SCHEDULES = "schedules_json";

    private final SharedPreferences prefs;

    public ScheduleStore(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public synchronized List<Schedule> getAllSchedules() {
        List<Schedule> list = new ArrayList<>();
        String jsonStr = prefs.getString(KEY_SCHEDULES, null);
        if (jsonStr == null || jsonStr.isEmpty()) {
            return list;
        }

        try {
            JSONArray array = new JSONArray(jsonStr);
            for (int i = 0; i < array.length(); i++) {
                Schedule schedule = Schedule.fromJsonObject(array.getJSONObject(i));
                list.add(schedule);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public synchronized Schedule getScheduleById(String id) {
        if (id == null) return null;
        for (Schedule s : getAllSchedules()) {
            if (id.equals(s.getId())) {
                return s;
            }
        }
        return null;
    }

    public synchronized void saveSchedule(Schedule schedule) {
        List<Schedule> schedules = getAllSchedules();
        int existingIndex = -1;
        for (int i = 0; i < schedules.size(); i++) {
            if (schedules.get(i).getId().equals(schedule.getId())) {
                existingIndex = i;
                break;
            }
        }

        if (existingIndex >= 0) {
            schedules.set(existingIndex, schedule);
        } else {
            schedules.add(schedule);
        }

        saveAllSchedules(schedules);
    }

    public synchronized void deleteSchedule(String id) {
        List<Schedule> schedules = getAllSchedules();
        List<Schedule> updated = new ArrayList<>();
        for (Schedule s : schedules) {
            if (!s.getId().equals(id)) {
                updated.add(s);
            }
        }
        saveAllSchedules(updated);
    }

    private synchronized void saveAllSchedules(List<Schedule> schedules) {
        JSONArray array = new JSONArray();
        for (Schedule s : schedules) {
            try {
                array.put(s.toJsonObject());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        prefs.edit().putString(KEY_SCHEDULES, array.toString()).apply();
    }
}
