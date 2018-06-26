package com.system2override.yoke;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.system2override.yoke.models.LocalTask;
import com.system2override.yoke.models.LocalTaskDao;
import com.system2override.yoke.models.MyTypeConverters;
import com.system2override.yoke.models.TodoApp;
import com.system2override.yoke.models.TodoAppDao;
import com.system2override.yoke.models.PerAppTodoRule;
import com.system2override.yoke.models.PerAppTodoRuleDao;

@Database(version = 4, entities = {PerAppTodoRule.class, TodoApp.class, LocalTask.class})
@TypeConverters({MyTypeConverters.class})
public abstract class HarnessDatabase extends RoomDatabase {
    public abstract TodoAppDao todoAppDao();
    public abstract PerAppTodoRuleDao perAppTodoRuleDao();

    public abstract LocalTaskDao localTaskDao();


}
