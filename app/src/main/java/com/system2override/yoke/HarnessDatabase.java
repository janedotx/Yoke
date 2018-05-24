package com.system2override.yoke;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(version = 1, entities = {TodoRule.class, TodoApp.class})
public abstract class HarnessDatabase extends RoomDatabase {
    public abstract TodoAppDao todoAppDao();
    public abstract TodoRuleDao todoRuleDao();


}
