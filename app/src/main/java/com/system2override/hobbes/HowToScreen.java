package com.system2override.hobbes;

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

import com.system2override.hobbes.TodoManagement.TodoManagementScreen;
import com.system2override.hobbes.UsageHistory.UsageHistoryScreen;

import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

public class HowToScreen extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "HowToScreen";
    private ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_screen);

        View next = findViewById(R.id.welcomeNextButton);
        next.setOnClickListener(this);

        this.bar = getSupportActionBar();
        this.bar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        if (!hasNoUsageStatsPermission(this)) {
            startActivity(new Intent(this, AfterHowToScreen.class));
        }
        */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private boolean hasNoUsageStatsPermission(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo appInfo = packageManager.getApplicationInfo(MyApplication.packageName, 0);
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, appInfo.uid, context.getPackageName());
            return mode != MODE_ALLOWED;
        }  catch (PackageManager.NameNotFoundException e ) {
            Log.d(TAG, "checkForUsageStatsPermission: oops wrong package name");
            return false;
        }
    }
}