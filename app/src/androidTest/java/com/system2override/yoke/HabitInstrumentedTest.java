package com.system2override.yoke;

import android.support.test.runner.AndroidJUnit4;

import com.google.api.client.util.DateTime;
import com.system2override.yoke.models.Habit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    public void testCompletedToday() {
        long anHourAgo = System.currentTimeMillis() - 1000 * 60 * 60;
        Habit newHabit = mTestDbWrapper.getDb().habitDao().getAllHabitsSince(anHourAgo).get(0);
        assertEquals(true, newHabit.completedToday());

        Habit oldHabit = mTestDbWrapper.getDb().habitDao().getAllHabitsBefore(anHourAgo).get(0);
        assertEquals(false, oldHabit.completedToday());
    }
}
