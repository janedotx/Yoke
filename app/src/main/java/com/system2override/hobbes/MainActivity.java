package com.system2override.hobbes;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.support.v4.app.AppOpsManagerCompat.MODE_ALLOWED;


import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;


import com.google.api.services.tasks.TasksScopes;
import com.squareup.otto.Subscribe;
import com.system2override.hobbes.ConfigScreens.BannedAppManagement.BannedAppScreen;
import com.system2override.hobbes.ConfigScreens.SetUsageLimitsScreen;
import com.system2override.hobbes.Models.Streaks;
import com.system2override.hobbes.Models.TimeBank;
import com.system2override.hobbes.OttoMessages.TimeBankEarnedTime;
import com.system2override.hobbes.TodoManagement.TodoManagementScreen;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "MainActivity";
    GoogleAccountCredential mCredential;
    public static final String[] SCOPES = { TasksScopes.TASKS_READONLY };
    private TextView mOutputText;
    private Button mCallApiButton;
    private TextView timeAvailableView;

    ProgressDialog mProgress;


    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Tasks API";
    private static final String PREF_ACCOUNT_NAME = "accountName";

    private String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Tasks API ...");

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

*/
        ((Button)findViewById(R.id.todoManagementScreenButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TodoManagementScreen.class);
                startActivity(i);
            }
        });

        ((Button) findViewById(R.id.mainBannedAppsButton)).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(MainActivity.this, BannedAppScreen.class);
               startActivity(i);
           }
        });

        ((Button) findViewById(R.id.resetStreakButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeBank timeBank = MyApplication.getTimeBank();
                Streaks streak = MyApplication.getStreaks();

                HarnessDatabase db = MyApplication.getDb();
                timeBank.resetTime();
                timeBank.putOneoffTimeGrant(0);
                timeBank.putDailyHabitTimeGrant(0);
                streak.endStreakDay();
            }
        });
        setupTimeBank();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkForUsageStatsPermission(this);
//        checkForAccessFineLocation(this);
//        checkForGoogleTasksPermission(this);


    }

    private void setupTimeBank() {
        this.timeAvailableView = findViewById(R.id.mainViewTimeAvailable);
        TimeBank timeBank = MyApplication.getTimeBank();
        this.timeAvailableView.setText("Available time is " + Long.toString(timeBank.getTotalTimeForToday()/1000L));

        Button initialButton = findViewById(R.id.initialTimeButtonSave);
        initialButton.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent i = new Intent(MainActivity.this, SetUsageLimitsScreen.class);
                                                 startActivity(i);
                                             }
                                         }
        );


        Button resetTimeBank = findViewById(R.id.resetTimeBank);
        resetTimeBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getTimeBank().resetTime();
            }
        });

    }
    private void setupHabits() {

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
            ApplicationInfo appInfo = packageManager.getApplicationInfo(MyApplication.packageName, 0);
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, appInfo.uid, context.getPackageName());
            if (mode != MODE_ALLOWED) {
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }
        }  catch (PackageManager.NameNotFoundException e ) {
            Log.d(TAG, "checkForUsageStatsPermission: oops wrong package name");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TimeBank timeBank = MyApplication.getTimeBank();
        long remainingTime = timeBank.getTimeRemaining();
        Log.d(TAG, "onResume: remaining time " + Long.toString(remainingTime));
        Log.d(TAG, "onResume: spent time " + Long.toString(timeBank.getSpentTime()));
        TextView remainingTimeView = findViewById(R.id.mainTimeRemainingView);
        remainingTimeView.setText("Remaining time view is " + Long.toString(remainingTime/1000) + " seconds");
    }

    @Subscribe
    protected void updateTimeAvailableView(TimeBankEarnedTime event) {
        Log.d(TAG, "updateTimeAvailableView: ");
        TimeBank timeBank = MyApplication.getTimeBank();
        this.timeAvailableView.setText("Available time is " + Long.toString(timeBank.getTotalTimeForToday()));
    }
}
