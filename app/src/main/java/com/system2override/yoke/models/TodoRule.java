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
    private int initialTimeGrant;

    @ColumnInfo(name = "todoAppId")
    private int todoAppId;

    @ColumnInfo(name="refreshGrantTime")
    private int refreshGrantTime;

    public int getTodoAppId() {
        return todoAppId;
    }

    public void setTodoAppId(int todoAppId) {
        this.todoAppId = todoAppId;
    }

    public int getId() {
        return id;
    }

    public int getInitialTimeGrant() {
        return initialTimeGrant;
    }

    public int getRefreshGrantTime() {
        return refreshGrantTime;
    }

    public void setInitialTimeGrant(int initialTimeGrant) {
        this.initialTimeGrant = initialTimeGrant;
    }

    public void setRefreshGrantTime(int refreshGrantTime) {
        this.refreshGrantTime = refreshGrantTime;
    }
}
