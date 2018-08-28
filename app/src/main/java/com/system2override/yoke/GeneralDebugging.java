package com.system2override.yoke;

import android.util.Log;

import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.RoomModels.LocalTask;
import com.system2override.yoke.Models.RoomModels.Suggestion;

import java.util.List;

public abstract class GeneralDebugging {
    private static final String TAG = "GeneralDebugging";

    public static void printDb(HarnessDatabase db) {
        Log.d(TAG, "printDb: ----------");
        Log.d(TAG, "printDb: about to print out this whole database");
        List<Habit> habits = db.habitDao().loadAllHabits();
        List<LocalTask> localTasks = db.localTaskDao().loadAllLocalTasks();
        List<Suggestion> suggestions = db.suggestionDao().loadAllSuggestions();

        Log.d(TAG, "printDb: habits");
        for (Habit h: habits) {
            Log.d(TAG, "printDb: a habit " + h.toString());
        }
        Log.d(TAG, " ----------");

        Log.d(TAG, "printDb: localtasks");
        for (LocalTask l: localTasks) {
            Log.d(TAG, "printDb: localtask " + l.toString());
        }

        Log.d(TAG, "printDb: suggestions");
        for (Suggestion s: suggestions) {
            Log.d(TAG, "printDb: suggestion " + s.text);
        }

        Log.d(TAG, "printDb: ----------");

    }
}
