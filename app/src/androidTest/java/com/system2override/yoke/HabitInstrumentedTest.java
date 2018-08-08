package com.system2override.yoke;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.system2override.yoke.Models.RoomModels.Habit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class HabitInstrumentedTest {
    private static final String TAG = "HabitInstrumentedTest";
    TestDbWrapper mTestDbWrapper;

    @Before
    public void setUpDb() {
        mTestDbWrapper = new TestDbWrapper();
        mTestDbWrapper.setUpFixtures();
    }

    @Test
    public void testCompletedOn() {
        Calendar todayObj = new GregorianCalendar();
        todayObj.setTimeInMillis(System.currentTimeMillis());

        String anHourAgo = Habit.convertMSToYYMMDD(System.currentTimeMillis() - 1000 * 60 * 60);
        Habit newHabit = mTestDbWrapper.getDb().habitDao().getAllHabitsSince(anHourAgo).get(0);
        assertEquals(true, newHabit.completedOn(todayObj));

        Habit oldHabit = mTestDbWrapper.getDb().habitDao().getFirstHabitByMatchingDescription("i was completed a million years ago");
        assertEquals(false, oldHabit.completedOn(todayObj));

        int size = mTestDbWrapper.getDb().habitDao().loadAllHabits().size();
        Log.d(TAG, "testCompletedToday: size " + Integer.toString(size));

        Habit neverDoneHabit = mTestDbWrapper.getDb().habitDao().getFirstHabitByMatchingDescription("never");
        assertEquals(false, neverDoneHabit.completedOn(todayObj));
        assertEquals("", neverDoneHabit.getLastDateCompleted());
    }


}
