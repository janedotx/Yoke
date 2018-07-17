package com.system2override.yoke;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.system2override.yoke.models.Habit;
import com.system2override.yoke.models.LocalTaskDao;
import com.system2override.yoke.models.LocalTask;
import com.system2override.yoke.models.PerAppTodoRule;
import com.system2override.yoke.models.TodoApp;
import com.system2override.yoke.models.TodoRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestDbWrapper extends AndroidTestCase {
    private static final String TAG = "TestDbWrapper";
//    /*
    private LocalTaskDao mTaskDao;
    private HarnessDatabase mDb;
    private Context context;

    public TestDbWrapper() {
        this.context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(BuildConfig.DATABASE_FILE);
        mDb = MyApplication.getDb(context);
    }

    public void setUpFixtures() {
        TodoApp app = new TodoApp();
        app.setTodoAppName("test_gtasks");
        mDb.todoAppDao().insert(app);
        TodoApp app1 = mDb.todoAppDao().getTodoAppFromName("test_gtasks");

        PerAppTodoRule rule = new PerAppTodoRule();
        rule.setTodoAppId(app1.getId());
        rule.setTime(30000);
        rule.setPackageName("com.android.contacts");
        mDb.perAppTodoRuleDao().insert(rule);

        List<LocalTask> localTaskList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            LocalTask localTask = createFixtureTask(app1, i);
            localTaskList.add(localTask);
        }
        mDb.localTaskDao().insertLocalTasksList(localTaskList);

        TodoRule todoRule = new TodoRule();
        todoRule.setInitialTimeGrant(1000);
        todoRule.setRefreshGrantTime(2000);
        todoRule.setTodoAppId(app1.getId());

        mDb.todoRuleDao().insert(todoRule);

        Habit oldHabit = new Habit();

        oldHabit.lastDateCompleted = 1_000_000_000_000L;
        oldHabit.description = "i was completed a million years ago";
        mDb.habitDao().insert(oldHabit);

        Habit newHabit = new Habit();
        newHabit.lastDateCompleted = System.currentTimeMillis();
        newHabit.description = "i was completed a million years ago";
        mDb.habitDao().insert(newHabit);
    }

    public LocalTask createFixtureTask(TodoApp todoApp, int i) {
        LocalTask localTask = new LocalTask();
        localTask.setTodoAppId(todoApp.getId());
        localTask.setDescription("description " + Integer.toString(i));
        localTask.setCompleted(false);
        localTask.setTodoAppName(todoApp.getTodoAppName());
        localTask.setUpdatedAt(new DateTime(10000 + (3600000 * i)).toStringRfc3339());
        localTask.setTodoAppIdString(todoApp.getTodoAppName() + Integer.toString(i));
        return localTask;
    }

    public void tearDown() throws IOException {
        mDb.close();
        context.deleteDatabase(BuildConfig.DATABASE_FILE);
    }

    public HarnessDatabase getDb() { return mDb; }

}
