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
        TimeBank.setInitialTime(this.context, 1000);
        TimeBank.setRewardTimeGrant(this.context, 2000);
        TimeBank.resetTime(this.context);

    }

    @Test
    public void testAddSpentTime() {
        TimeBank.addSpentTime(this.context, 1000L);
        long curTime = TimeBank.getSpentTime(InstrumentationRegistry.getTargetContext());
        assertEquals(1000L, curTime);
    }

    @Test
    public void testAddRewardGrant() {
        TimeBank.earnTime(this.context);
        long curAvailableTime = TimeBank.getAvailableTime(this.context);
        assertEquals(3000, curAvailableTime);
    }

    @Test
    public void testSettersAndGetters() {
        TimeBank.setInitialTime(this.context, 1000);
        TimeBank.setRewardTimeGrant(this.context, 2000);
        assertEquals(1000, TimeBank.getInitialTime(this.context));
        assertEquals(2000, TimeBank.getRewardTimeGrant(this.context));
    }

    @Test
    public void testResetTime() {
        TimeBank.addSpentTime(this.context, 1000L);
        long curTime = TimeBank.getSpentTime(this.context);
        assertEquals(1000L, curTime);

        TimeBank.earnTime(this.context);
        long curAvailableTime = TimeBank.getAvailableTime(this.context);
        assertEquals(3000, curAvailableTime);

        TimeBank.resetTime(this.context);
        assertEquals(0, TimeBank.getSpentTime(this.context));
        assertEquals(1000, TimeBank.getAvailableTime(this.context));
//        */
    }

    @Test
    public void testGetEarnedTime() {
        assertEquals(0L, TimeBank.getTotalEarnedTimeToday(this.context));
        TimeBank.earnTime(this.context);
        assertEquals(2000L, TimeBank.getTotalEarnedTimeToday(this.context));
        TimeBank.earnTime(this.context);
        assertEquals(4000L, TimeBank.getTotalEarnedTimeToday(this.context));

        TimeBank.resetTime(this.context);
        assertEquals(0L, TimeBank.getTotalEarnedTimeToday(this.context));

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
