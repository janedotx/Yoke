package com.system2override.yoke.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.system2override.yoke.models.TodoApp;

import java.util.List;

@Dao
public interface TodoAppDao {
    @Insert
    long insert(TodoApp todoApp);

    @Query("SELECT * FROM TodoApps")
    public List<TodoApp> loadAllTodoApps();

    @Query("SELECT * from TodoApps where todoapp_name = :name LIMIT 1")
    public TodoApp getTodoAppFromName(String name);
}
