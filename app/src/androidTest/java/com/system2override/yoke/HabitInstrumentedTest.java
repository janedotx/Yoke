package com.system2override.yoke;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.RoomModels.HabitDao;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
    public void testGetHabitsOnly() {
        HabitDao dao = mTestDbWrapper.getDb().habitDao();
        List<Habit> loadAllHabitsTest = dao.loadAllHabits();
        assertEquals(4, loadAllHabitsTest.size());

        List<Habit> allOneOffs = dao.loadAllOneOffs();
        assertEquals(2, allOneOffs.size());

        List<Habit> oneOneOffs = dao.loadNumOneOffs(1);
        assertEquals(1, oneOneOffs.size());

        List<Habit> numIncompleteHabits = dao.loadNumIncompleteHabits(1);
        assertEquals(1, numIncompleteHabits.size());

        List<Habit> allIncompletes = dao.loadAllIncompleteHabits();
        assertEquals(3, allIncompletes.size());

        List<Habit> allCompleteHabits = dao.loadAllCompleteHabits();
        assertEquals(1, allCompleteHabits.size());

        List<Habit> allIncompleteOneOffs = dao.loadAllIncompleteOneOffs();
        assertEquals(1, allIncompleteOneOffs.size());

        List<Habit> loadAllCompleteOneOffs = dao.loadAllCompleteOneOffs();
        assertEquals(1, loadAllCompleteOneOffs.size());

    }



}
