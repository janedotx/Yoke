package com.system2override.yoke;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.system2override.yoke.models.LocalTaskDao;
import com.system2override.yoke.models.LocalTask;
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

@RunWith(AndroidJUnit4.class)
public class TestDbWrapper extends AndroidTestCase {
    private static final String TAG = "TestDbWrapper";
//    /*
    private LocalTaskDao mTaskDao;
    private HarnessDatabase mDb;

    public TestDbWrapper() {
        Context context = InstrumentationRegistry.getContext();
        mDb = Room.inMemoryDatabaseBuilder(context, HarnessDatabase.class)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public void setUpFixtures() {
        TodoApp app = new TodoApp();
        app.setTodoAppName("test_gtasks");
        mDb.todoAppDao().insert(app);
        TodoApp app1 = mDb.todoAppDao().getTodoAppFromName("test_gtasks");

        TodoRule rule = new TodoRule();
        rule.setTodoAppId(app1.getId());
        rule.setTime(30000);
        rule.setPackageName("com.android.contacts");
        mDb.todoRuleDao().insert(rule);

        List<LocalTask> localTaskList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            LocalTask localTask = createFixtureTask(app1, i);
            localTaskList.add(localTask);
        }
        mDb.localTaskDao().insertLocalTasksList(localTaskList);

    }

    public LocalTask createFixtureTask(TodoApp todoApp, int i) {
        LocalTask localTask = new LocalTask();
        localTask.setTodoAppId(todoApp.getId());
        localTask.setDescription("description " + Integer.toString(i));
        localTask.setCompleted(false);
        localTask.setTodoAppName(todoApp.getTodoAppName());
        localTask.setUpdatedAt(new DateTime(10000 + (1000 * i)).toStringRfc3339());
        localTask.setTodoAppIdString(todoApp.getTodoAppName() + Integer.toString(i));
        return localTask;
    }

    public void close() throws IOException {
        mDb.close();
    }

    public HarnessDatabase getDb() { return mDb; }

}
