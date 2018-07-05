package com.system2override.yoke;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.system2override.yoke.OttoMessages.CurrentAppMessage;
import com.system2override.yoke.models.TodoRule;

public class RulesManager {
    private static final String TAG = "RulesManager";
    private Context context;
    private TodoRule todoRule;
    private Intent launcherIntent;

    public RulesManager(Context context, HarnessDatabase db) {
        this.context = context;
        this.todoRule = db.todoRuleDao().getTodoRule();
        this.launcherIntent = new Intent("android.intent.action.MAIN");
        this.launcherIntent.addCategory("android.intent.category.HOME");

        MyApplication.getBus().register(this);
    }

    @com.squareup.otto.Subscribe
    public void checkForRuleTriggers(CurrentAppMessage currentAppMessage) {
        // TODO fix
        long currentTime = TimeBank.getAvailableTime(this.context);
        if (currentTime >= todoRule.getInitialTimeGrant()) {
            this.context.startActivity(this.launcherIntent);
        }

    }

}
