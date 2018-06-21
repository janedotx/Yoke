package com.system2override.yoke;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.system2override.yoke.models.LocalTask;
import com.system2override.yoke.models.TodoApp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DBInstrumentedTest {
    private static final String TAG = "ExampleInstrumentedTest";
    TestDbWrapper mTestDbWrapper;

    @Before
    public void setUpDb() {
        mTestDbWrapper = new TestDbWrapper();
        mTestDbWrapper.setUpFixtures();
    }

    @After
    public void closeDb() throws IOException {
        mTestDbWrapper.close();
    }

    @Test
    public void insertsWorkProperly() {
        assertEquals(3, mTestDbWrapper.getDb().localTaskDao().loadAllLocalTasks().size());
        List<LocalTask> tasks = mTestDbWrapper.getDb().localTaskDao().loadAllLocalTasks();
        Log.d(TAG, "insertsWorkProperly: " + tasks.get(0).toString());
        assertEquals(1, mTestDbWrapper.getDb()
                .localTaskDao()
                .getLocalTasksForAppSince("test_gtasks", new DateTime(11000).toStringRfc3339()).size());
        assertEquals(3, mTestDbWrapper.getDb()
                .localTaskDao()
                .getIncompleteLocalTasksForApp("test_gtasks").size());
    }

    @Test public void completeTypeConverterWorks() {
        LocalTask t = mTestDbWrapper.getDb().localTaskDao().getLocalTasksByID(1);
        assertEquals(false, t.isCompleted());
        t.completed = true;
        mTestDbWrapper.getDb().localTaskDao().update(t);
        LocalTask t1 = mTestDbWrapper.getDb().localTaskDao().getLocalTasksByID(1);
        assertEquals(true, t1.isCompleted());

    }
}
