package com.system2override.hobbes;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.system2override.hobbes.Models.BannedApps;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.util.Set;

@RunWith(AndroidJUnit4.class)
public class BannedAppsTest {
    private static final String TAG = "BannedAppsTest";
    Context context;
    BannedApps bannedApps;

    @Before
    public void setUp() {
        this.context = InstrumentationRegistry.getTargetContext();
        bannedApps = new BannedApps(this.context);
        bannedApps.clearApps();
    }

    @Test
    public void testBannedAppsAppManagement() {
        Set<String> apps = bannedApps.getApps();
        assertEquals(0, apps.size());

        bannedApps.addApp("com.package.foo");
        bannedApps.addApp("com.package.foo2");

        Set<String> freshApps = bannedApps.getApps();
        assertEquals(2, freshApps.size());

        assertTrue(freshApps.contains("com.package.foo"));
        assertTrue(freshApps.contains("com.package.foo2"));

        bannedApps.removeApp("com.package.foo");

        Set<String> freshApps2 = bannedApps.getApps();
        assertEquals(1, freshApps2.size());

        assertTrue(freshApps2.contains("com.package.foo2"));

    }

    @After
    public void tearDown() {
        bannedApps.clearApps();
    }

}
