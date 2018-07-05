package com.system2override.yoke;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TimeBankTest {
    private static final String TAG = "TimeBankTest";
    Context context;
    TestDbWrapper mTestDbWrapper;

    @Before
    public void setUp() throws IOException {
        this.context = InstrumentationRegistry.getTargetContext();
        mTestDbWrapper = new TestDbWrapper();
        mTestDbWrapper.setUpFixtures();
        TimeBank.resetTime(this.context);
    }

    @Test
    public void testAddTime() {
        TimeBank.addSpentTime(this.context, 1000L);
        long curTime = TimeBank.getSpentTime(InstrumentationRegistry.getTargetContext());
        assertEquals(1000L, curTime);
    }

    @Test
    public void testResetTime() {
//        /*
        TimeBank.addSpentTime(this.context, 1000L);
        long curTime = TimeBank.getSpentTime(this.context);
        assertEquals(1000L, curTime);

        TimeBank.addRefreshGrant(this.context);
        long curAvailableTime = TimeBank.getAvailableTime(this.context);
        assertEquals(3000, curAvailableTime);

        TimeBank.resetTime(this.context);
        long curSpentTime = TimeBank.getSpentTime(this.context);
        assertEquals(0, curSpentTime);
        long availableTime = TimeBank.getAvailableTime(this.context);
        assertEquals(1000, availableTime);
//        */
    }

    @After
    public void tearDown() {
        TimeBank.resetTime(this.context);
        try {
            mTestDbWrapper.tearDown();
        } catch (IOException e) {
            System.out.println("DB unexpectedly not there!");
        }
    }
}
