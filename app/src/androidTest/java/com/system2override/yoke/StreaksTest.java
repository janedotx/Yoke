package com.system2override.yoke;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
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
    Streaks streak;

    @Before
    public void setUp() {
        mTestDbWrapper = new TestDbWrapper();
        mTestDbWrapper.setUpFixtures();
        this.context = InstrumentationRegistry.getTargetContext();
        streak = new Streaks(this.context, new Bus(ThreadEnforcer.ANY));
        streak.setStreakCompletedToday(false);
        streak.setLongestStreak(0);
        streak.setStreakCompletedToday(false);
        streak.setCurrentStreak(0);
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
        List<Habit> habits = mTestDbWrapper.getDb().habitDao().loadAllHabits();
        assertFalse(streak.canAddStreak(habits));
        completeAllHabits();
        List<Habit> habits2 = mTestDbWrapper.getDb().habitDao().loadAllHabits();
        assertTrue(streak.canAddStreak(habits2));

    }

    @Test
    public void testSettersGetters() {
        assertEquals(0, streak.getCurrentStreak());
        assertEquals(0, streak.getLongestStreak());

        streak.setCurrentStreak(1);
        assertEquals(1, streak.getCurrentStreak());

        streak.setLongestStreak(1);
        assertEquals(1, streak.getLongestStreak());

        assertFalse(streak.getStreakCompletedToday());
        streak.setStreakCompletedToday(true);
        assertTrue(streak.getStreakCompletedToday());
    }


    @Test
    public void testUpdateStreakInformation() {
        // ensure that a streak is extended when all dailies are checked off
        List<Habit> habits = mTestDbWrapper.getDb().habitDao().loadAllHabits();
        streak.updateStreakInformation(habits);
        completeAllHabits();
        List<Habit> habits3 = mTestDbWrapper.getDb().habitDao().loadAllHabits();
        streak.updateStreakInformation(habits3);
        assertEquals(true, streak.getStreakCompletedToday());
        assertEquals(1, streak.getCurrentStreak());
        assertEquals(1, streak.getLongestStreak());

        // ensure that a streak that was extended when all dailies are checked off is shortened
        // when a daily is unchecked
        Habit first = habits.get(0);
        first.setCompleted(false);
        mTestDbWrapper.getDb().habitDao().update(first);
        List<Habit> habits2 = mTestDbWrapper.getDb().habitDao().loadAllHabits();
        streak.updateStreakInformation(habits2);
        assertEquals(false, streak.getStreakCompletedToday());
        assertEquals(0, streak.getCurrentStreak());
        assertEquals(0, streak.getLongestStreak());
    }

    @Test
    public void testResetStreakInformation() {
        // case where daily habits weren't completed, so the current streak is reset to zero
        List<Habit> habits = mTestDbWrapper.getDb().habitDao().loadAllHabits();
        streak.setCurrentStreak(1);
        streak.setLongestStreak(1);
        streak.updateStreakInformation(habits);
        assertFalse(streak.getStreakCompletedToday());
        streak.endStreakDay();
        assertEquals(0, streak.getCurrentStreak());

        // case where all daily habits were completed, and current streak is not reset to zero
        completeAllHabits();
        List<Habit> habits1 = mTestDbWrapper.getDb().habitDao().loadAllHabits();
        streak.updateStreakInformation(habits1);

        List<Habit> habits2 = mTestDbWrapper.getDb().habitDao().loadAllHabits();
        assertTrue(streak.getStreakCompletedToday());
        assertEquals(1, streak.getLongestStreak());
        streak.endStreakDay();
        assertFalse(streak.getStreakCompletedToday());
        assertEquals(1, streak.getLongestStreak());

    }

    @Test
    public void testLongestStreak() {
        // hard to do without mocking out time, i don't feel like doing that right now
    }
}
