package com.system2override.yoke.Models.RoomModels;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface HabitDao {
    @Insert
    long insert(Habit habit);

    @Insert
    List<Long> insert(Habit... habits);

    @Update
    public void updateHabits(Habit... habits);

    @Update
    public void updateHabits(List<Habit> habits);

    @Update
    public void update(Habit habit);

    @Delete
    public void delete(Habit h);

    @Query("DELETE FROM Habits WHERE id = :id")
    public void delete(int id);

    @Delete
    public void delete(Habit... habits);

    // load all dailies
    @Query("SELECT * FROM Habits where isDailyHabit = 1")
    public List<Habit> loadAllHabits();

    @Query("SELECT * from Habits where isDailyHabit = 0")
    public List<Habit> loadAllOneOffs();

    @Query("SELECT * FROM Habits where isDailyHabit = 1 AND completed = 0  LIMIT :limit")
    public List<Habit> loadNumIncompleteHabits(int limit);

    @Query("SELECT * from Habits where isDailyHabit = 0 LIMIT :limit")
    public List<Habit> loadNumOneOffs(int limit);

    @Query("SELECT * FROM Habits where completed = 0 AND isDailyHabit = 1")
    public List<Habit> loadAllIncompleteHabits();

    @Query("SELECT * FROM Habits where completed = 1 AND isDailyHabit = 1")
    public List<Habit> loadAllCompleteHabits();

    @Query("SELECT * FROM Habits where completed = 0 AND isDailyHabit = 0")
    public List<Habit> loadAllIncompleteOneOffs();

    @Query("SELECT * FROM Habits where completed = 0 AND isDailyHabit = 0 LIMIT :limit")
    public List<Habit> loadNumIncompleteOneOffs(int limit);

    @Query("SELECT * FROM Habits where completed = 1 AND isDailyHabit = 0")
    public List<Habit> loadAllCompleteOneOffs();

    @Query("SELECT * FROM Habits WHERE id = :id")
    public Habit getById(int id);

}

