package com.system2override.yoke.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AppDao {
    @Insert
    long insert(App naughtyApp);

    @Query("SELECT * FROM Apps")
    public List<TodoApp> loadAllApps();
}
