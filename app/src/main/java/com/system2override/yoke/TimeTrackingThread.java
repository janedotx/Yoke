package com.system2override.yoke;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.system2override.yoke.models.TodoRule;

import java.util.HashMap;
import java.util.List;

public class TimeTrackingThread extends Thread {
    private static final String TAG = "RulesManagerThread";
    private final long SLEEP_LENGTH = 2000;
    Context context;
    UsageStatsManager manager;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    List<TodoRule> rules;
    HashMap<String, List<TodoRule>> packageNameToRulesMap;
    ActivityManager activityManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TimeTrackingThread(Context context) {
        this.context = context;
        this.sharedPrefs = context.getSharedPreferences(context.getString(R.string.time_tracking_file), Context.MODE_PRIVATE);
        this.editor = sharedPrefs.edit();
        this.packageNameToRulesMap = new HashMap<String, List<TodoRule>>();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void run() {
        activityManager = (ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            while(true) {
                getForegroundApp();
                Thread.sleep(SLEEP_LENGTH);
            }
        } catch (InterruptedException e) {
            Log.d(TAG, "run: this shouldn't happen since i have not written any other threads to interrupt this thread yet");
            Log.d(TAG, "run: " + e.getStackTrace());
        }
    }

    // sadly this doesn't work on android 8.0
    private RunningAppProcessInfo getForegroundApp() {

        List<ActivityManager.RunningAppProcessInfo> list = this.activityManager.getRunningAppProcesses();
        if (list == null) {
            Log.d(TAG, "getForegroundApp: twas null");
            return null;
        }
        for (int i = 0; i < list.size(); i++) {
            Log.d(TAG, "getForegroundApp: " + list.get(i).processName);
        }
        return list.get(0);
    }
}
