package com.system2override.yoke;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.Streaks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class StreaksTest {
    private static final String TAG = "HabitInstrumentedTest";
    TestDbWrapper mTestDbWrapper;
    Context context;

    @Before
    public void setUp() {
        mTestDbWrapper = new TestDbWrapper();
        mTestDbWrapper.setUpFixtures();
        this.context = InstrumentationRegistry.getTargetContext();
        Streaks.setStreakCompletedToday(context, false);
        Streaks.setLongestStreak(context, 0);
        Streaks.setStreakCompletedToday(context, false);
        Streaks.setCurrentStreak(context, 0);
    }

    private void completeAllHabits() {
        List<Habit> habits = mTestDbWrapper.getDb().habitDao().loadAllHabits();
        for (Habit h: habits) {
            h.lastDateCompleted = Habit.convertMSToYYMMDD(System.currentTimeMillis());
        }
        mTestDbWrapper.getDb().habitDao().updateHabits(habits);
    }

    @Test
    public void addStreakTest() {
        assertFalse(Streaks.canAddStreak(mTestDbWrapper.getDb()));
        completeAllHabits();
        assertTrue(Streaks.canAddStreak(mTestDbWrapper.getDb()));

    }

    @Test
    public void testSettersGetters() {
        assertEquals(0, Streaks.getCurrentStreak(context));
        assertEquals(0, Streaks.getLongestStreak(context));

        Streaks.setCurrentStreak(context, 1);
        assertEquals(1, Streaks.getCurrentStreak(context));

        Streaks.setLongestStreak(context, 1);
        assertEquals(1, Streaks.getLongestStreak(context));

        assertFalse(Streaks.getStreakCompletedToday(context));
        Streaks.setStreakCompletedToday(context, true);
        assertTrue(Streaks.getStreakCompletedToday(context));
    }


    @Test
    public void testUpdateStreakInformation() {
        Streaks.updateStreakInformation(context, mTestDbWrapper.getDb());
        completeAllHabits();
        Streaks.updateStreakInformation(context, mTestDbWrapper.getDb());
    }

    @Test
    public void testResetStreakInformation() {
        // case where daily habits weren't completed, so the current streak is reset to zero
        Streaks.setCurrentStreak(context, 1);
        Streaks.updateStreakInformation(context, mTestDbWrapper.getDb());
        assertFalse(Streaks.getStreakCompletedToday(context));
        Streaks.resetStreakInformation(context);
        assertEquals(0, Streaks.getCurrentStreak(context));

        // case where all daily habits were completed, and current streak is not reset to zero
        completeAllHabits();
        Streaks.updateStreakInformation(context, mTestDbWrapper.getDb());

        assertTrue(Streaks.getStreakCompletedToday(context));
        assertEquals(1, Streaks.getLongestStreak(context));
        Streaks.resetStreakInformation(context);
        assertFalse(Streaks.getStreakCompletedToday(context));
        assertEquals(1, Streaks.getLongestStreak(context));

    }

    @Test
    public void testLongestStreak() {
        // hard to do without mocking out time, i don't feel like doing that right now
    }
}
