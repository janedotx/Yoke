package com.system2override.yoke;

import android.app.AppOpsManager;
import static android.support.v4.app.AppOpsManagerCompat.MODE_ALLOWED;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.system2override.yoke.TodoManagement.TodoManagementScreen;

import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

public class HowToScreen extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "HowToScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_screen);

        View next = findViewById(R.id.welcomeNextButton);
        next.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasNoUsageStatsPermission(this)) {
            Log.d(TAG, "onResume: permission has been granted");
            startActivity( new Intent(this, TodoManagementScreen.class));
        };
    }

    @Override
    public void onClick(View v) {
        if (hasNoUsageStatsPermission(this)) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        } else {
            startActivity( new Intent(this, TodoManagementScreen.class));
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
