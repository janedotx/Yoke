package com.system2override.yoke;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.system2override.yoke.models.Habit;
import com.system2override.yoke.models.HabitDao;
import com.system2override.yoke.models.LocalTask;
import com.system2override.yoke.models.LocalTaskDao;
import com.system2override.yoke.models.MyTypeConverters;

@Database(version = 8, entities = {LocalTask.class, Habit.class})
@TypeConverters({MyTypeConverters.class})
public abstract class HarnessDatabase extends RoomDatabase {
    public abstract LocalTaskDao localTaskDao();
    public abstract HabitDao habitDao();

}
