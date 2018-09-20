package com.system2override.yoke;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.system2override.yoke.Models.BannedApps;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.RoomModels.LocalTask;
import com.system2override.yoke.Models.RoomModels.Suggestion;
import com.system2override.yoke.Models.Streaks;
import com.system2override.yoke.Models.TimeBank;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    public static Bus bus;
    public static String packageName;
    public static String googleAPIClientId = "1052545727002-i62d0brehmtb9lc4b2teta3rbognvrmf.apps.googleusercontent.com";
    public static TimeBank timeBank;
    public static BannedApps bannedApps;
    public static Streaks streaks;

    public static String MIDNIGHT_RESET_ACTION = "MIDNIGHT_RESET";

    public static RoomDatabase.Builder<HarnessDatabase> databaseBuilder;
    public static HarnessDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        bus = new Bus(ThreadEnforcer.ANY);
        packageName = getApplicationContext().getPackageName();
        timeBank = new TimeBank(this, bus);
        bannedApps = new BannedApps(this);
        streaks = new Streaks(this, bus);
        setupDB();
//        setupBannedApps();

//        writeLogCat();
    }

    public static TimeBank getTimeBank() { return timeBank; }
    public static BannedApps getBannedApps() { return bannedApps; }
    public static Streaks getStreaks() { return streaks; }

    public static Bus getBus() {
        return bus;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static HarnessDatabase getDb(Context context) {
//        /*
        if (db == null) {
            db = Room.databaseBuilder(context,
                HarnessDatabase.class, BuildConfig.DATABASE_FILE)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return db;
 //       */
        /*
        return Room.databaseBuilder(context,
                HarnessDatabase.class, BuildConfig.DATABASE_FILE)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                     .build();
                     */

    }

    // bad to have this running while debugging, because logcat no longer prints to stdout and you can't
    // see it in Android Studio
    public void writeLogCat() {
            File path = new File(this.getExternalFilesDir(null), "logcat.log");
        Log.d(TAG, "writeLogCat: PATH " + path);
            try {
                Process process = Runtime.getRuntime().exec("logcat -c");
                process = Runtime.getRuntime().exec("logcat -r 10000 -f " + path.getAbsolutePath());
            } catch ( IOException e ) {
                e.printStackTrace();
            }
    }

    public static Calendar getYesterdayCalObj() {
        Calendar yesterdayCalObj = new GregorianCalendar();
        yesterdayCalObj.setTimeInMillis(System.currentTimeMillis());
        yesterdayCalObj.add(Calendar.DAY_OF_YEAR, -1);
        return yesterdayCalObj;
    }

    public static Calendar getTodayCalObj() {
        Calendar todayCalObj = new GregorianCalendar();
        todayCalObj.setTimeInMillis(System.currentTimeMillis());
        return todayCalObj;
    }

    private void setupDB() {
        boolean success = this.deleteDatabase(BuildConfig.DATABASE_FILE);
//        boolean success = this.deleteDatabase("db");
        Log.d(TAG, "onStart: database successfully deleted " + Boolean.toString(success));

        HarnessDatabase db = MyApplication.getDb(this);

        //       /*
        Habit newHabit1 = new Habit();
        newHabit1.description = "do pushups";

        Habit newHabit2 = new Habit();
        newHabit2.description = "stretch";

        Habit newHabit3 = new Habit();
        newHabit3.description = "do yoga";

        Habit newHabit4 = new Habit();
        newHabit4.description = "sketch something";

        Habit newHabit5 = new Habit();
        newHabit5.description = "longlonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglong";

        db.habitDao().insert(newHabit1, newHabit2, newHabit3, newHabit4, newHabit5);

        /*
        for (int i = 0; i < 300;  i++) {
            Habit brandNewHabit = new Habit();
            brandNewHabit.description = "brandNewHabit " + Integer.toString(i);
            db.habitDao().insert(brandNewHabit);
        }
        */

        LocalTask localTask = new LocalTask();
        localTask.setDescription("im a fake imported task");
        db.localTaskDao().insert(localTask);

        Suggestion suggestion1 = new Suggestion();
        suggestion1.text = "drink more water";

        Suggestion suggestion2 = new Suggestion();
        suggestion2.text = "go out dancing";

        Suggestion suggestion3 = new Suggestion();
        suggestion3.text = "clean the bathroom";

        db.suggestionDao().insert(suggestion1, suggestion2, suggestion3);

        GeneralDebugging.printDb(db);

    }

    private void setupBannedApps() {
        bannedApps.clearApps();
        bannedApps.addApp( "com.twitter.android");
        bannedApps.addApp( "com.instagram.android");
    }

}
