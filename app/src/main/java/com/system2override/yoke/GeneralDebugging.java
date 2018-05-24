package com.system2override.yoke;

import android.util.Log;

import java.util.List;

public abstract class GeneralDebugging {
    private static final String TAG = "GeneralDebugging";

    public static void printDb(HarnessDatabase db) {
        Log.d(TAG, "printDb: ----------");
        Log.d(TAG, "printDb: about to print out this whole database");
        List<TodoApp> todoApps  = db.todoAppDao().loadAllTodoApps();
        List<TodoRule> todoRules  = db.todoRuleDao().loadAllTodoRules();

        Log.d(TAG, "printDb: about to print entries of TodoApps");
        for (int i = 0; i < todoApps.size(); i++) {
            Log.d(TAG, "printDb: " + todoApps.get(i).toString());
        }

        Log.d(TAG, "printDb: about to print entries of TodoRules");
        for (int i = 0; i < todoRules.size(); i++) {
            Log.d(TAG, "printDb: " + todoRules.get(i).toString());
        }
        Log.d(TAG, "printDb: ----------");

    }
}
