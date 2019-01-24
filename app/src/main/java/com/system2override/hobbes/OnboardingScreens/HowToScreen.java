package com.system2override.hobbes.OnboardingScreens;

import android.app.AppOpsManager;
import static android.support.v4.app.AppOpsManagerCompat.MODE_ALLOWED;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.system2override.hobbes.R;
import com.system2override.hobbes.TodoManagement.TodoManagementScreen;
import com.system2override.hobbes.UsageHistory.UsageHistoryScreen;

import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

public class HowToScreen extends OnboardingActivity implements View.OnClickListener {
    private static final String TAG = "HowToScreen";
    private ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.next.setOnClickListener(this);
        this.textView.setText(R.string.howToText);

    }

    @Override
    public void onClick(View v) {
        if (hasNoUsageStatsPermission(this)) {
            startActivity( new Intent(this, AfterHowToScreen.class));
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        } else {
            startActivity( new Intent(this, AfterHowToScreen.class));
        }

    }
}
