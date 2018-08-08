package com.system2override.yoke;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.system2override.yoke.Models.RoomModels.LocalTask;

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
public class ExampleInstrumentedTest {
    private static final String TAG = "ExampleInstrumentedTest";
    TestDbWrapper mTestDbWrapper;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.system2override.yoke", appContext.getPackageName());
    }

    @Before
    public void setUpDb() {
        mTestDbWrapper = new TestDbWrapper();
        mTestDbWrapper.setUpFixtures();
    }

    @After
    public void closeDb() throws IOException {
        mTestDbWrapper.tearDown();
    }

    @Test
    // plus multiple tasks?
    // this is the case where we had tasks t1, t2, t3, and t3 was done, so should return true
    public void verifyTaskWasCompleted(){
        List<LocalTask> apiTasks = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            LocalTask localTask = mTestDbWrapper.createFixtureTask(i);
            apiTasks.add(localTask);
        }
    }

    @Test
    public void verifyTaskWasNotCompleted() {

    }

    @Test
    public void verifyTaskWasUpdated() {

    }

}
