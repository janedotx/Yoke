package com.system2override.yoke;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TodoAppDao {
    @Insert
    long insert(TodoApp todoApp);

    @Query("SELECT * FROM TodoApps")
    public List<TodoApp> loadAllTodoApps();

    @Query("SELECT * from TodoApps where :name = todoapp_name LIMIT 1")
    public TodoApp getTodoAppFromName(String name);
}
