package com.example.systemscheduler;

import android.content.Context;

import com.example.systemscheduler.data.ScheduleStore;
import com.example.systemscheduler.model.ActionType;
import com.example.systemscheduler.model.CapabilityLevel;
import com.example.systemscheduler.model.OperationType;
import com.example.systemscheduler.model.RepeatType;
import com.example.systemscheduler.model.Schedule;
import com.example.systemscheduler.operation.OperationDispatcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class ScheduleUnitTest {

    private Context context;
    private ScheduleStore scheduleStore;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.getApplication();
        scheduleStore = new ScheduleStore(context);
    }

    @Test
    public void testScheduleStoreSaveAndRetrieve() {
        Schedule s1 = new Schedule(OperationType.WIFI, ActionType.ON, 8, 30, RepeatType.DAILY);
        scheduleStore.saveSchedule(s1);

        List<Schedule> all = scheduleStore.getAllSchedules();
        assertEquals(1, all.size());

        Schedule retrieved = scheduleStore.getScheduleById(s1.getId());
        assertNotNull(retrieved);
        assertEquals(OperationType.WIFI, retrieved.getOperation());
        assertEquals(ActionType.ON, retrieved.getAction());
        assertEquals(8, retrieved.getHour());
        assertEquals(30, retrieved.getMinute());
        assertEquals(RepeatType.DAILY, retrieved.getRepeatType());
    }

    @Test
    public void testScheduleStoreDelete() {
        Schedule s1 = new Schedule(OperationType.BLUETOOTH, ActionType.OFF, 22, 0, RepeatType.DAILY);
        scheduleStore.saveSchedule(s1);

        assertEquals(1, scheduleStore.getAllSchedules().size());

        scheduleStore.deleteSchedule(s1.getId());
        assertEquals(0, scheduleStore.getAllSchedules().size());
    }

    @Test
    public void testNextTriggerTimeDaily() {
        Schedule s = new Schedule(OperationType.SYNC, ActionType.ON, 10, 0, RepeatType.DAILY);

        Calendar baseCal = Calendar.getInstance();
        baseCal.set(2025, Calendar.JANUARY, 1, 9, 0, 0);
        baseCal.set(Calendar.MILLISECOND, 0);

        long triggerTime = s.getNextTriggerTime(baseCal.getTimeInMillis());

        Calendar triggerCal = Calendar.getInstance();
        triggerCal.setTimeInMillis(triggerTime);

        assertEquals(2025, triggerCal.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, triggerCal.get(Calendar.MONTH));
        assertEquals(1, triggerCal.get(Calendar.DAY_OF_MONTH));
        assertEquals(10, triggerCal.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, triggerCal.get(Calendar.MINUTE));
    }

    @Test
    public void testNextTriggerTimeNextDayIfPast() {
        Schedule s = new Schedule(OperationType.SYNC, ActionType.ON, 8, 0, RepeatType.DAILY);

        Calendar baseCal = Calendar.getInstance();
        baseCal.set(2025, Calendar.JANUARY, 1, 9, 0, 0);
        baseCal.set(Calendar.MILLISECOND, 0);

        long triggerTime = s.getNextTriggerTime(baseCal.getTimeInMillis());

        Calendar triggerCal = Calendar.getInstance();
        triggerCal.setTimeInMillis(triggerTime);

        assertEquals(2, triggerCal.get(Calendar.DAY_OF_MONTH));
        assertEquals(8, triggerCal.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void testOperationCapabilities() {
        assertEquals(CapabilityLevel.DIRECT, OperationDispatcher.getCapability(context, OperationType.SYNC));
        assertEquals(CapabilityLevel.USER_ACTION_REQUIRED, OperationDispatcher.getCapability(context, OperationType.LOCATION));
        assertEquals(CapabilityLevel.PRIVILEGED, OperationDispatcher.getCapability(context, OperationType.REBOOT));
        assertEquals(CapabilityLevel.PRIVILEGED, OperationDispatcher.getCapability(context, OperationType.SHUTDOWN));
    }
}
