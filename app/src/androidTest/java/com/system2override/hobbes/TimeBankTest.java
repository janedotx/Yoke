package com.system2override.hobbes;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.system2override.hobbes.Models.TimeBank;

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
        long curAvailableTime = timeBank.getEarnedTime();
        assertEquals(2000, curAvailableTime);
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
        long curAvailableTime = timeBank.getTotalTimeForToday();
        assertEquals(3000, curAvailableTime);

        timeBank.resetTime();
        assertEquals(0, timeBank.getSpentTime());
        assertEquals(1000, timeBank.getTotalTimeForToday());
//        */
    }

    @Test
    public void testGetEarnedTime() {
        assertEquals(0L, timeBank.getEarnedTime());
        timeBank.earnTime();
        assertEquals(2000L, timeBank.getEarnedTime());
        timeBank.earnTime();
        assertEquals(4000L, timeBank.getEarnedTime());

        timeBank.resetTime();
        assertEquals(0L, timeBank.getEarnedTime());

    }

    @Test
    public void testGetTotalTimeForToday() {
        assertEquals(1000L, timeBank.getTotalTimeForToday());
        timeBank.earnTime();
        assertEquals(3000L, timeBank.getTotalTimeForToday());

    }

    @Test
    public void getTimeRemaining() {
        timeBank.earnTime();
        timeBank.earnTime();
        timeBank.earnTime();
        timeBank.addSpentTime(3000);

        assertEquals(5000L, timeBank.getTimeRemaining());
    }

}
