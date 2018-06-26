package com.system2override.yoke.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PerAppTodoRuleDao {
    @Insert
    long insert(PerAppTodoRule todoRule);

    @Query("SELECT * FROM PerAppTodoRules")
    public List<PerAppTodoRule> loadAllPerAppTodoRules();

    @Query("SELECT * from PerAppTodoRules WHERE packageName = :name")
    public List<PerAppTodoRule> getPerAppRulesForPackageName(String name);

    @Query("SELECT * FROM PerAppTodoRules WHERE packageName = :name ORDER BY time ASC LIMIT 1")
    public PerAppTodoRule getStrictestRuleForPackageName(String name);

    @Query("SELECT * from PerAppTodoRules where :id = id")
    public PerAppTodoRule getRuleFromId(int id);

}
