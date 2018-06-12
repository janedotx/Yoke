package com.system2override.yoke.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.time.Instant;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "LocalTasks",
        foreignKeys = {@ForeignKey(entity = TodoApp.class,
                                            parentColumns = "id",
                                            childColumns="todoappId",
                                            onDelete = CASCADE)},
        indices = {@Index(name = "todoApp_index", value = "todoappId"),
                    @Index(value = "packageName")}
        )
public class LocalTask {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="completed")
    public boolean completed;

    // RFC 3339 (which is in UTC)
    @ColumnInfo(name="dateCompleted")
    public String dateCompleted;

    @ColumnInfo(name = "todoAppId")
    public int todoAppId;

    @ColumnInfo(name="todoApp")
    public String todoApp;

    // RFC 3339 (which is in UTC)
    @ColumnInfo(name="updatedAt")
    public String updatedAt;

    public int getId() {
        return id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public int getTodoAppId() {
        return todoAppId;
    }

    public void setTodoAppId(int todoAppId) {
        this.todoAppId = todoAppId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTodoApp() {
        return todoApp;
    }

    public void setTodoApp(String todoApp) {
        this.todoApp = todoApp;
    }
}
