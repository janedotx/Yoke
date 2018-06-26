package com.system2override.yoke.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import com.system2override.yoke.models.Rule;
import com.system2override.yoke.models.TodoApp;

// though i dont need indices, as i anticipate there being like five todo apps max
@Entity(tableName="PerAppTodoRules",
        foreignKeys = {@ForeignKey(entity = TodoApp.class,
                                            parentColumns = "id",
                                            childColumns="todoAppId",
                                            onDelete = CASCADE)},
        indices = {@Index(name = "todoAppIndex", value = "todoAppId"),
                    @Index(value = "packageName")}
        )
public class PerAppTodoRule extends Rule {
    // bad app
    @android.support.annotation.NonNull
    @ColumnInfo(name="packageName")
    private String packageName;

    // this is in milliseconds
    @ColumnInfo(name="time")
    private int time;

    @ColumnInfo(name = "todoAppId")
    private int todoAppId;

    public int getTodoAppId() {
        return todoAppId;
    }

    public void setTodoAppId(int todoAppId) {
        this.todoAppId = todoAppId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @NonNull
    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(@NonNull String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "PerAppTodoRule{" +
                "id=" + this.id +
                ", packageName='" + packageName + '\'' +
                ", time=" + time +
                ", todoAppId=" + todoAppId +
                '}';
    }
}
