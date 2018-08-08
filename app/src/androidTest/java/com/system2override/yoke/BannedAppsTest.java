package com.system2override.yoke;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.system2override.yoke.Models.BannedApps;

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

    @Before
    public void setUp() {
        this.context = InstrumentationRegistry.getTargetContext();
        BannedApps.clearApps(this.context);
    }

    @Test
    public void testBannedAppsAppManagement() {
        Set<String> apps = BannedApps.getApps(context);
        assertEquals(0, apps.size());

        BannedApps.addApp(context, "com.package.foo");
        BannedApps.addApp(context, "com.package.foo2");

        Set<String> freshApps = BannedApps.getApps(context);
        assertEquals(2, freshApps.size());

        assertTrue(freshApps.contains("com.package.foo"));
        assertTrue(freshApps.contains("com.package.foo2"));

    }

    @After
    public void tearDown() {
        BannedApps.clearApps(this.context);
    }

}
