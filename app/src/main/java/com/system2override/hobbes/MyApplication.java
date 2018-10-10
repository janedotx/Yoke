package com.system2override.hobbes;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.system2override.hobbes.Models.BannedApps;
import com.system2override.hobbes.Models.BeforeHobbesAppData;
import com.system2override.hobbes.Models.OneTimeData;
import com.system2override.hobbes.Models.RoomModels.Habit;
import com.system2override.hobbes.Models.RoomModels.Suggestion;
import com.system2override.hobbes.Models.Streaks;
import com.system2override.hobbes.Models.TimeBank;

import java.io.File;
import java.io.IOException;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    public static Bus bus;
    public static String packageName;
    public static String googleAPIClientId = "1052545727002-i62d0brehmtb9lc4b2teta3rbognvrmf.apps.googleusercontent.com";
    public static TimeBank timeBank;
    public static BannedApps bannedApps;
    public static Streaks streaks;
    public static OneTimeData oneTimeData;
    public static BeforeHobbesAppData beforeHobbesAppData;

    public static String MIDNIGHT_RESET_ACTION = "MIDNIGHT_RESET";

    public static RoomDatabase.Builder<HarnessDatabase> databaseBuilder;
    public static HarnessDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        bus = new Bus(ThreadEnforcer.ANY);
        packageName = getApplicationContext().getPackageName();
        timeBank = new TimeBank(this, bus);
        timeBank.putDailyHabitTimeGrant(TimeBank.MAX_REFRESH_DAILY_TIME);
        timeBank.putOneoffTimeGrant(TimeBank.MAX_REFRESH_ONE_OFF_TIME);
        bannedApps = new BannedApps(this);
        streaks = new Streaks(this, bus);
        oneTimeData = new OneTimeData(this);
        beforeHobbesAppData = new BeforeHobbesAppData(this);


        // to delete db
//        File file = new File(BuildConfig.DATABASE_FILE);
//        boolean success = SQLiteDatabase.deleteDatabase(file);

        db =  Room.databaseBuilder(getApplicationContext(),
                HarnessDatabase.class, BuildConfig.DATABASE_FILE)
                    .allowMainThreadQueries()
//                    .fallbackToDestructiveMigration()
                    .build();
        startManagerService();
//        setupDB();
        firstTimeSetup();
    }


    public static TimeBank getTimeBank() { return timeBank; }
    public static BannedApps getBannedApps() { return bannedApps; }
    public static Streaks getStreaks() { return streaks; }
    public static OneTimeData getOneTimeData() { return oneTimeData; }
    public static BeforeHobbesAppData getBeforeHobbesAppData() { return beforeHobbesAppData; }
    public static Bus getBus() {
        return bus;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static HarnessDatabase getDb() {
        return db;
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

    private void setupDB() {

        HarnessDatabase db = MyApplication.getDb();

        //       /*
        Habit newHabit1 = new Habit();
        newHabit1.description = "do pushups";
        newHabit1.isDailyHabit = true;

        Habit newHabit2 = new Habit();
        newHabit2.description = "stretch";
        newHabit2.isDailyHabit = true;

        Habit newHabit3 = new Habit();
        newHabit3.description = "do yoga";

        Habit newHabit4 = new Habit();
        newHabit4.description = "sketch something";

        Habit newHabit5 = new Habit();
        newHabit5.description = "longlonglonglonglonglong";

        db.habitDao().insert(newHabit1, newHabit2, newHabit3, newHabit4, newHabit5);

        /*
        for (int i = 0; i < 300;  i++) {
            Habit brandNewHabit = new Habit();
            brandNewHabit.description = "brandNewHabit " + Integer.toString(i);
            db.habitDao().insert(brandNewHabit);
        }
        */


        GeneralDebugging.printDb(db);

    }

    private void setUpSuggestions() {

        HarnessDatabase db = MyApplication.getDb();
        Suggestion suggestion1 = new Suggestion();
        suggestion1.text = "Drink more water";

        Suggestion suggestion2 = new Suggestion();
        suggestion2.text = "Stretch for ten minutes";

        Suggestion suggestion3 = new Suggestion();
        suggestion3.text = "Meditate for five minutes";

        Suggestion suggestion4 = new Suggestion();
        suggestion4.text = "Walk the dog";

        Suggestion suggestion5 = new Suggestion();
        suggestion5.text = "Get groceries";

        db.suggestionDao().insert(suggestion1, suggestion2, suggestion3);

    }


    private void setupBannedApps() {
        bannedApps.clearApps();
        bannedApps.addApp( "com.twitter.android");
        bannedApps.addApp( "com.instagram.android");
    }

    private void setupTimeBank() {
        timeBank.setInitialTime(30 * 60 * 1000);
        timeBank.setRewardTimeGrant(15 * 60 * 1000);
//        timeBank.setInitialTime(1 * 60 * 1000);
//        timeBank.setRewardTimeGrant(1 * 60 * 1000);
        timeBank.resetTime();
    }

    public void firstTimeSetup() {
        if (oneTimeData.getTimeOfHobbesInstall() == 0) {
            setUpSuggestions();
            oneTimeData.setTimeOfHobbesInstall(System.currentTimeMillis());
            // set the time to be backdated 24 hours from the actual time of install, for testing purposes
//            oneTimeData.setTimeOfHobbesInstall(System.currentTimeMillis() - UsageStatsHelper.DAY_IN_MS - 1);

        }
    }

    public static boolean inTutorial() {
        return !oneTimeData.getHasDoneOnboardingKey();
    }

    // according to docs, this is idempotent
    private void startManagerService() {
        Intent intent = new Intent(this, ManagerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

}
