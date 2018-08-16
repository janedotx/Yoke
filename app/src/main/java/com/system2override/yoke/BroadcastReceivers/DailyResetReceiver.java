package com.system2override.yoke.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.Streaks;
import com.system2override.yoke.Models.TimeBank;
import com.system2override.yoke.MyApplication;

import java.util.List;

public class DailyResetReceiver extends BroadcastReceiver{
    private static final String TAG = "DailyResetReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        TimeBank timeBank = MyApplication.getTimeBank();
        Streaks streak = MyApplication.getStreaks();

        if (MyApplication.MIDNIGHT_RESET_ACTION.equals(intent.getAction())) {
            List<Habit> habits = MyApplication.getDb(context).habitDao().loadAllHabits();
            timeBank.resetTime(context);
            streak.endStreakDay(habits);
            Toast.makeText(context, "Midnight reset happened",
                    Toast.LENGTH_LONG).show();
        }
    }
}
