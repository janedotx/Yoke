package com.system2override.yoke.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.Streaks;
import com.system2override.yoke.Models.TimeBank;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.OttoMessages.MidnightResetEvent;

import java.util.List;

public class DailyResetReceiver extends BroadcastReceiver{
    private static final String TAG = "DailyResetReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        TimeBank timeBank = MyApplication.getTimeBank();
        Streaks streak = MyApplication.getStreaks();

        if (MyApplication.MIDNIGHT_RESET_ACTION.equals(intent.getAction())) {
            HarnessDatabase db = MyApplication.getDb();
            List<Habit> habits = db.habitDao().loadAllHabits();
            for (Habit h: habits) {
                h.setCompleted(false);
            }
            db.habitDao().updateHabits(habits);
            timeBank.resetTime();
            streak.endStreakDay();

            MyApplication.getBus().post(new MidnightResetEvent());
        }
    }
}
