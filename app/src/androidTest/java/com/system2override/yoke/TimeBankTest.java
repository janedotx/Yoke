package com.system2override.yoke;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.system2override.yoke.Models.TimeBank;

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
    TimeBank timeBank;

    @Before
    public void setUp() throws IOException {
        this.context = InstrumentationRegistry.getTargetContext();
        mTestDbWrapper = new TestDbWrapper();
        mTestDbWrapper.setUpFixtures();
        this.timeBank = new TimeBank(this.context, new Bus(ThreadEnforcer.ANY));
        timeBank.setInitialTime(this.context, 1000);
        timeBank.setRewardTimeGrant(this.context, 2000);
        timeBank.resetTime(this.context);

    }

    @Test
    public void testAddSpentTime() {
        timeBank.addSpentTime(this.context, 1000L);
        long curTime = timeBank.getSpentTime(InstrumentationRegistry.getTargetContext());
        assertEquals(1000L, curTime);
    }

    @Test
    public void testAddRewardGrant() {
        timeBank.earnTime(this.context);
        long curAvailableTime = timeBank.getAvailableTime();
        assertEquals(3000, curAvailableTime);
    }

    @Test
    public void testSettersAndGetters() {
        timeBank.setInitialTime(this.context, 1000);
        timeBank.setRewardTimeGrant(this.context, 2000);
        assertEquals(1000, timeBank.getInitialTime(this.context));
        assertEquals(2000, timeBank.getRewardTimeGrant(this.context));
    }

    @Test
    public void testResetTime() {
        timeBank.addSpentTime(this.context, 1000L);
        long curTime = timeBank.getSpentTime(this.context);
        assertEquals(1000L, curTime);

        timeBank.earnTime(this.context);
        long curAvailableTime = timeBank.getAvailableTime();
        assertEquals(3000, curAvailableTime);

        timeBank.resetTime(this.context);
        assertEquals(0, timeBank.getSpentTime(this.context));
        assertEquals(1000, timeBank.getAvailableTime());
//        */
    }

    @Test
    public void testGetEarnedTime() {
        assertEquals(0L, timeBank.getTotalEarnedTimeToday(this.context));
        timeBank.earnTime(this.context);
        assertEquals(2000L, timeBank.getTotalEarnedTimeToday(this.context));
        timeBank.earnTime(this.context);
        assertEquals(4000L, timeBank.getTotalEarnedTimeToday(this.context));

        timeBank.resetTime(this.context);
        assertEquals(0L, timeBank.getTotalEarnedTimeToday(this.context));

    }

    @After
    public void tearDown() {
        timeBank.resetTime(this.context);
        try {
            mTestDbWrapper.tearDown();
        } catch (IOException e) {
            System.out.println("DB unexpectedly not there!");
        }
    }
}
