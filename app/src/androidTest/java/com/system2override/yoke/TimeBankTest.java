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
        timeBank.setInitialTime(1000);
        timeBank.setRewardTimeGrant(2000);
        timeBank.resetTime();

    }

    @Test
    public void testAddSpentTime() {
        timeBank.addSpentTime(1000L);
        long curTime = timeBank.getSpentTime();
        assertEquals(1000L, curTime);
    }

    @Test
    public void testAddRewardGrant() {
        timeBank.earnTime();
        long curAvailableTime = timeBank.getAvailableTime();
        assertEquals(3000, curAvailableTime);
    }

    @Test
    public void testSettersAndGetters() {
        timeBank.setInitialTime(1000);
        timeBank.setRewardTimeGrant(2000);
        assertEquals(1000, timeBank.getInitialTime());
        assertEquals(2000, timeBank.getRewardTimeGrant());
    }

    @Test
    public void testResetTime() {
        timeBank.addSpentTime(1000L);
        long curTime = timeBank.getSpentTime();
        assertEquals(1000L, curTime);

        timeBank.earnTime();
        long curAvailableTime = timeBank.getAvailableTime();
        assertEquals(3000, curAvailableTime);

        timeBank.resetTime();
        assertEquals(0, timeBank.getSpentTime());
        assertEquals(1000, timeBank.getAvailableTime());
//        */
    }

    @Test
    public void testGetEarnedTime() {
        assertEquals(0L, timeBank.getTotalEarnedTimeToday());
        timeBank.earnTime();
        assertEquals(2000L, timeBank.getTotalEarnedTimeToday());
        timeBank.earnTime();
        assertEquals(4000L, timeBank.getTotalEarnedTimeToday());

        timeBank.resetTime();
        assertEquals(0L, timeBank.getTotalEarnedTimeToday());

    }

    @After
    public void tearDown() {
        timeBank.resetTime();
        try {
            mTestDbWrapper.tearDown();
        } catch (IOException e) {
            System.out.println("DB unexpectedly not there!");
        }
    }
}
