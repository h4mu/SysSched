package com.example.systemscheduler.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

public class Schedule {
    private String id;
    private OperationType operation;
    private ActionType action;
    private int hour;
    private int minute;
    private RepeatType repeatType;
    private int dayOfWeek; // 1 (Sun) to 7 (Sat) according to Calendar.DAY_OF_WEEK, used if WEEKLY
    private boolean enabled;
    private long createdAt;
    private long lastExecution;
    private String lastResultStatus;

    public Schedule() {
        this.id = UUID.randomUUID().toString();
        this.enabled = true;
        this.createdAt = System.currentTimeMillis();
        this.repeatType = RepeatType.DAILY;
        this.dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public Schedule(OperationType operation, ActionType action, int hour, int minute, RepeatType repeatType) {
        this();
        this.operation = operation;
        this.action = action;
        this.hour = hour;
        this.minute = minute;
        this.repeatType = repeatType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public RepeatType getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(RepeatType repeatType) {
        this.repeatType = repeatType;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastExecution() {
        return lastExecution;
    }

    public void setLastExecution(long lastExecution) {
        this.lastExecution = lastExecution;
    }

    public String getLastResultStatus() {
        return lastResultStatus;
    }

    public void setLastResultStatus(String lastResultStatus) {
        this.lastResultStatus = lastResultStatus;
    }

    /**
     * Calculates the next execution epoch millis starting from a reference time.
     */
    public long getNextTriggerTime(long now) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(now);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);

        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);

        if (repeatType == RepeatType.ONCE) {
            if (cal.getTimeInMillis() <= now) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
        } else if (repeatType == RepeatType.DAILY) {
            if (cal.getTimeInMillis() <= now) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
        } else if (repeatType == RepeatType.WEEKLY) {
            int currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int daysUntilTarget = (dayOfWeek - currentDayOfWeek + 7) % 7;
            if (daysUntilTarget == 0 && cal.getTimeInMillis() <= now) {
                daysUntilTarget = 7;
            }
            cal.add(Calendar.DAY_OF_YEAR, daysUntilTarget);
        }
        return cal.getTimeInMillis();
    }

    public JSONObject toJsonObject() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("operation", operation != null ? operation.name() : null);
        json.put("action", action != null ? action.name() : null);
        json.put("hour", hour);
        json.put("minute", minute);
        json.put("repeatType", repeatType != null ? repeatType.name() : null);
        json.put("dayOfWeek", dayOfWeek);
        json.put("enabled", enabled);
        json.put("createdAt", createdAt);
        json.put("lastExecution", lastExecution);
        json.put("lastResultStatus", lastResultStatus);
        return json;
    }

    public static Schedule fromJsonObject(JSONObject json) throws JSONException {
        Schedule s = new Schedule();
        s.setId(json.getString("id"));
        if (json.has("operation") && !json.isNull("operation")) {
            s.setOperation(OperationType.valueOf(json.getString("operation")));
        }
        if (json.has("action") && !json.isNull("action")) {
            s.setAction(ActionType.valueOf(json.getString("action")));
        }
        s.setHour(json.getInt("hour"));
        s.setMinute(json.getInt("minute"));
        if (json.has("repeatType") && !json.isNull("repeatType")) {
            s.setRepeatType(RepeatType.valueOf(json.getString("repeatType")));
        }
        s.setDayOfWeek(json.optInt("dayOfWeek", Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));
        s.setEnabled(json.getBoolean("enabled"));
        s.setCreatedAt(json.optLong("createdAt", System.currentTimeMillis()));
        s.setLastExecution(json.optLong("lastExecution", 0));
        s.setLastResultStatus(json.optString("lastResultStatus", null));
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(id, schedule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
