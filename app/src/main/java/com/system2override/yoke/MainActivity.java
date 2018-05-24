package com.system2override.yoke;

import android.app.AppOpsManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.PACKAGE_USAGE_STATS;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.support.v4.app.AppOpsManagerCompat.MODE_ALLOWED;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkForUsageStatsPermission(this);
        checkForAccessFineLocation(this);

        Intent intent = new Intent(this, ManagerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.util.Log.d(TAG, "subscribeToSensor: about to start foreground service " + Long.toString(System.currentTimeMillis()));
            startForegroundService(intent);
        } else {
            android.util.Log.d(TAG, "subscribeToSensor: about to start service " + Long.toString(System.currentTimeMillis()));
            startService(intent);
        }


        HarnessDatabase db = Room.databaseBuilder(getApplicationContext(),
                HarnessDatabase.class, "db").allowMainThreadQueries().build();
        /*
        TodoApp app = new TodoApp();

        app.setTodoAppName("gtasks");
        db.todoAppDao().insert(app);

        app = db.todoAppDao().getTodoAppFromName(app.getTodoAppName());

        TodoRule rule = new TodoRule();
        rule.setTodoappId(app.getId());
        rule.setTime(30);
        rule.setPackageName("com.android.contacts");
        db.todoRuleDao().insert(rule);
//        */

        GeneralDebugging.printDb(db);
/*        ArrayList<TodoRule> rules = new ArrayList<>(Arrays.asList(db.todoRuleDao().loadAllTodoRules()));
        for (int i = 0; i < rules.size(); i++) {
            Log.d(TAG, "onStart: " + rules.get(i).toString());
        }
        */
        db.close();
        RulesManager.foo();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: i was called");
    }

    private void checkForAccessFineLocation(Context context) {
        int hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);

        if (hasAccessFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: about to ask permission");
            ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION}, 1);
        }
    }

    // this only works with api 22+
    private void checkForUsageStatsPermission(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo appInfo = packageManager.getApplicationInfo("com.system2override.yoke", 0);
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, appInfo.uid, context.getPackageName());
            if (mode != MODE_ALLOWED) {
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }
        }  catch (PackageManager.NameNotFoundException e ) {
            Log.d(TAG, "checkForUsageStatsPermission: oops wrong package name");
        }
    }
}
