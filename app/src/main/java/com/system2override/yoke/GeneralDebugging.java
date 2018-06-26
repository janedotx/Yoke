package com.system2override.yoke;

import android.util.Log;

import com.system2override.yoke.models.TodoApp;
import com.system2override.yoke.models.PerAppTodoRule;

import java.util.List;

public abstract class GeneralDebugging {
    private static final String TAG = "GeneralDebugging";

    public static void printDb(HarnessDatabase db) {
        Log.d(TAG, "printDb: ----------");
        Log.d(TAG, "printDb: about to print out this whole database");
        List<TodoApp> todoApps  = db.todoAppDao().loadAllTodoApps();
        List<PerAppTodoRule> perAppTodoRules  = db.perAppTodoRuleDao().loadAllPerAppTodoRules();

        Log.d(TAG, "printDb: about to print entries of TodoApps");
        for (int i = 0; i < todoApps.size(); i++) {
            Log.d(TAG, "printDb: " + todoApps.get(i).toString());
        }

        Log.d(TAG, "printDb: about to print entries of PerAppTodoRules");
        for (int i = 0; i < perAppTodoRules.size(); i++) {
            Log.d(TAG, "printDb: " + perAppTodoRules.get(i).toString());
        }
        Log.d(TAG, "printDb: ----------");

    }
}
