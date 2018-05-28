package com.system2override.yoke;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TodoRuleDao {
    @Insert
    long insert(TodoRule todoRule);

    @Query("SELECT * FROM TodoRules")
    public List<TodoRule> loadAllTodoRules();

    @Query("SELECT * from TodoRules WHERE :name = packageName")
    public List<TodoRule> getRulesForPackageName(String name);

    @Query("SELECT * from TodoRules where :id = id")
    public TodoRule getRuleFromId(int id);

}
