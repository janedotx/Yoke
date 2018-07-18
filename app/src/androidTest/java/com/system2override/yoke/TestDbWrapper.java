package com.system2override.yoke;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.AndroidTestCase;

import com.google.api.client.util.DateTime;
import com.system2override.yoke.models.Habit;
import com.system2override.yoke.models.LocalTaskDao;
import com.system2override.yoke.models.LocalTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestDbWrapper extends AndroidTestCase {
    private static final String TAG = "TestDbWrapper";
//    /*
    private LocalTaskDao mTaskDao;
    private HarnessDatabase mDb;
    private Context context;

    public TestDbWrapper() {
        this.context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(BuildConfig.DATABASE_FILE);
        mDb = MyApplication.getDb(context);
    }

    public void setUpFixtures() {

        List<LocalTask> localTaskList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            LocalTask localTask = createFixtureTask(i);
            localTask.setUpdatedAt(new DateTime(10000 + (i * 1000) ).toStringRfc3339());
            localTaskList.add(localTask);
        }
        mDb.localTaskDao().insertLocalTasksList(localTaskList);


        Habit oldHabit = new Habit();

        oldHabit.lastDateCompleted = 1_000_000_000_000L;
        oldHabit.description = "i was completed a million years ago";
        mDb.habitDao().insert(oldHabit);

        Habit newHabit = new Habit();
        newHabit.lastDateCompleted = System.currentTimeMillis();
        newHabit.description = "i was completed a million years ago";
        mDb.habitDao().insert(newHabit);
    }

    public LocalTask createFixtureTask(int i) {
        LocalTask localTask = new LocalTask();
        localTask.setDescription("description " + Integer.toString(i));
        localTask.setCompleted(false);
        localTask.setUpdatedAt(new DateTime(10000 + (3600000 * i)).toStringRfc3339());
        return localTask;
    }

    public void tearDown() throws IOException {
        mDb.close();
        context.deleteDatabase(BuildConfig.DATABASE_FILE);
    }

    public HarnessDatabase getDb() { return mDb; }

}
