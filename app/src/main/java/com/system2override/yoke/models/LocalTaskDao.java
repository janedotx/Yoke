package com.system2override.yoke.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface LocalTaskDao {
    @Insert
    long insert(LocalTask localTask);

    @Query("SELECT * FROM LocalTasks")
    public List<LocalTask> loadAllLocalTasks();

    @Query("SELECT * FROM LocalTasks WHERE updatedAt > :updatedAt")
    public List<LocalTask> getLocalTasksSince(String updatedAt);

    @Insert
    public void insertLocalTasks(LocalTask... localTasks);
}
