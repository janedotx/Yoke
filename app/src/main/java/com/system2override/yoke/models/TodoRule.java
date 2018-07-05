package com.system2override.yoke.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName="TodoRules",
        foreignKeys = {@ForeignKey(entity = TodoApp.class,
                parentColumns = "id",
                childColumns="todoAppId",
                onDelete = CASCADE)},
        indices = {@Index(value = "todoAppId")
                }
)
public class TodoRule extends Rule {
    // this is in milliseconds
    @ColumnInfo(name="initialTimeGrant")
    private long initialTimeGrant;

    @ColumnInfo(name = "todoAppId")
    private int todoAppId;

    @ColumnInfo(name="refreshGrantTime")
    private long refreshGrantTime;

    public int getTodoAppId() {
        return todoAppId;
    }

    public void setTodoAppId(int todoAppId) {
        this.todoAppId = todoAppId;
    }

    public int getId() {
        return id;
    }

    public long getInitialTimeGrant() {
        return initialTimeGrant;
    }

    public long getRefreshGrantTime() {
        return refreshGrantTime;
    }

    public void setInitialTimeGrant(long initialTimeGrant) {
        this.initialTimeGrant = initialTimeGrant;
    }

    public void setRefreshGrantTime(long refreshGrantTime) {
        this.refreshGrantTime = refreshGrantTime;
    }
}
