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

    @Query("SELECT * FROM Habits")
    public List<Habit> loadAllHabits();

    @Query("SELECT * FROM Habits where lastDateCompleted >= :lastDateCompleted")
    public List<Habit> getAllHabitsSince(String lastDateCompleted);

    @Query("SELECT * FROM Habits where lastDateCompleted >= :lastDateCompleted LIMIT :limit")
    public List<Habit> getAllHabitsSince(String lastDateCompleted, int limit);

    @Query("SELECT * FROM Habits where lastDateCompleted < :lastDateCompleted LIMIT :limit")
    public List<Habit> getAllHabitsBefore(String lastDateCompleted, int limit);

    @Query("SELECT * FROM Habits where lastDateCompleted < :lastDateCompleted")
    public List<Habit> getAllHabitsCompletedBefore(String lastDateCompleted);

    @Query("SELECT * FROM Habits WHERE description = :description")
    public List<Habit> getHabitsByMatchingDescription(String description);

    @Query("SELECT * FROM Habits WHERE description = :description LIMIT 1")
    public Habit getFirstHabitByMatchingDescription(String description);

    @Query("SELECT * FROM Habits WHERE id = :id")
    public Habit getById(int id);

}

