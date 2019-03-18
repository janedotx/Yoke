package com.system2override.hobbes.OnboardingScreens;

import android.os.Handler;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.system2override.hobbes.HasBottomNavScreen;
import com.system2override.hobbes.HobbesScreen;
import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.R;
import com.system2override.hobbes.UsageHistory.UsageHistoryScreen;

import org.junit.After;

import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.support.v4.app.AppOpsManagerCompat.MODE_ALLOWED;

public class AfterHowToScreen extends HasBottomNavScreen implements  View.OnClickListener {
    private static final String TAG = "AfterHowToScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_after_how_to_screen);
        super.onCreate(savedInstanceState);

        if (hasNoUsageStatsPermission(this)) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(AfterHowToScreen.this, UsageHistoryScreen.class));
        finish();
    }
}
