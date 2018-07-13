package com.system2override.yoke.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface LocalTaskDao {
    @Insert
    long insert(LocalTask localTask);

    @Query("SELECT * FROM LocalTasks")
    public List<LocalTask> loadAllLocalTasks();

    @Query("SELECT * FROM LocalTasks WHERE :todoAppName = todoAppName AND updatedAt > :updatedAt")
    public List<LocalTask> getLocalTasksForAppSince(String todoAppName, String updatedAt);

    @Query("SELECT * FROM LocalTasks WHERE :todoAppName = todoAppName AND completed = 0")
    public List<LocalTask> getIncompleteLocalTasksForApp(String todoAppName);

    @Query("SELECT * FROM LocalTasks WHERE :todoAppName = todoAppName AND completed = 1")
    public List<LocalTask> getCompletedLocalTasksForApp(String todoAppName);

    @Query("SELECT * FROM LocalTasks WHERE :todoAppName = todoAppName")
    public List<LocalTask> getLocalTasksForTodoApp(String todoAppName);

    @Query("SELECT * FROM LocalTasks WHERE :id = todoAppIdString")
    public LocalTask getLocalTasksByTodoAppIDString(String id);

    @Query("SELECT * FROM LocalTasks WHERE :id = id")
    public LocalTask getLocalTasksByID(int id);

    @Update
    public void update(LocalTask... task);

    @Insert
    public void insertLocalTasks(LocalTask... localTasks);

    @Insert
    public void insertLocalTasksList(List<LocalTask> localTasks);

    @Query("SELECT * FROM LocalTasks ORDER BY updatedAt DESC LIMIT 1")
    public LocalTask getMostRecentlyUpdatedLocalTask();
}
