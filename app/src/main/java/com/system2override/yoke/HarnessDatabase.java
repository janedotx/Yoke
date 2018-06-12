package com.system2override.yoke;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.system2override.yoke.models.TodoApp;
import com.system2override.yoke.models.TodoAppDao;
import com.system2override.yoke.models.TodoRule;
import com.system2override.yoke.models.TodoRuleDao;

@Database(version = 2, entities = {TodoRule.class, TodoApp.class})
public abstract class HarnessDatabase extends RoomDatabase {
    public abstract TodoAppDao todoAppDao();
    public abstract TodoRuleDao todoRuleDao();


}
