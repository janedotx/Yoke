package com.system2override.hobbes;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.system2override.hobbes.AppLimit.AppLimitScreenPresenter;
import com.system2override.hobbes.AppLimit.AppLimitTasks;
import com.system2override.hobbes.Models.RoomModels.Habit;
import com.system2override.hobbes.Models.RoomModels.LocalTask;
import com.system2override.hobbes.Models.ToDoInterface;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AppLimitTasksTest {
    private HarnessDatabase mDb;
    private Context context;


    @Before
    public void setupBeforeEach() {
        this.context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(BuildConfig.DATABASE_FILE);
        mDb = MyApplication.getDb();
    }

    @Test
    public void testGetAllHabits() {
        for (int i = 0; i < 3; i++) {
            Habit h = new Habit();
            mDb.habitDao().insert(h);
        }

        AppLimitTasks tasks = new AppLimitTasks(mDb);
        List<ToDoInterface> toDos = tasks.getToDos();
        assertEquals(3, tasks.getToDos().size());
        for (int i = 0; i < 3; i++) {
            assertEquals(Habit.class, toDos.get(i).getClass());
        }
    }

    @Test
    public void testGetMixOfThree() {
        Habit h = new Habit();
        mDb.habitDao().insert(h);

        LocalTask l = new LocalTask();
        mDb.localTaskDao().insert(l);

        LocalTask l2 = new LocalTask();
        mDb.localTaskDao().insert(l2);

        AppLimitTasks tasks = new AppLimitTasks(mDb);
        List<ToDoInterface> toDos = tasks.getToDos();
        assertEquals(3, tasks.getToDos().size());

        int numHabits = 0;
        int numLocalTasks = 0;
        for (int i = 0; i < 3; i++) {
            if (toDos.get(i).getClass().equals(Habit.class)) {
                numHabits++;
            }
            if (toDos.get(i).getClass().equals(LocalTask.class)) {
                numLocalTasks++;
            }
        }

        assertEquals(1, numHabits);
        assertEquals(2, numLocalTasks);
    }

    @Test
    public void getMixLessThanThree() {
        Habit h = new Habit();
        mDb.habitDao().insert(h);

        LocalTask l = new LocalTask();
        mDb.localTaskDao().insert(l);

        AppLimitTasks tasks = new AppLimitTasks(mDb);
        List<ToDoInterface> toDos = tasks.getToDos();
        assertEquals(2, tasks.getToDos().size());

        int numHabits = 0;
        int numLocalTasks = 0;
        for (int i = 0; i < tasks.getToDos().size(); i++) {
            if (toDos.get(i).getClass().equals(Habit.class)) {
                numHabits++;
            }
            if (toDos.get(i).getClass().equals(LocalTask.class)) {
                numLocalTasks++;
            }
        }

        assertEquals(1, numHabits);
        assertEquals(1, numLocalTasks);
    }


    @Test
    public void testGetAllTasks(){
        for (int i = 0; i < 3; i++) {
            LocalTask l = new LocalTask();
            mDb.localTaskDao().insert(l);
        }

        AppLimitTasks tasks = new AppLimitTasks(mDb);
        List<ToDoInterface> toDos = tasks.getToDos();
        assertEquals(3, tasks.getToDos().size());
        for (int i = 0; i < 3; i++) {
            assertEquals(LocalTask.class, toDos.get(i).getClass());
        }
    }
}
