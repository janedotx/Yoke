package com.system2override.yoke.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface HabitDao {
    @Insert
    long insert(Habit habit);

    @Query("SELECT * FROM Habits")
    public List<Habit> loadAllHabits();

    @Query("SELECT * FROM Habits where lastDateCompleted > :lastDateCompleted")
    public List<Habit> getAllHabitsSince(long lastDateCompleted);

    @Query("SELECT * FROM Habits where lastDateCompleted < :lastDateCompleted")
    public List<Habit> getAllHabitsBefore(long lastDateCompleted);
}
