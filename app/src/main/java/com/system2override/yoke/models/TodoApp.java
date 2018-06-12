package com.system2override.yoke.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

//TODO change todoapp_name to just 'name'
@Entity(tableName="TodoApps", indices = {@Index(value={"todoapp_name"}, unique = true)})
public class TodoApp {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "oauth2_token")
    private String oauth2Token;

    @NonNull
    @ColumnInfo(name = "todoapp_name")
    private String todoAppName;

    @NonNull
    public String getTodoAppName() {
        return todoAppName;
    }

    public void setTodoAppName(@NonNull String todoAppName) {
        this.todoAppName = todoAppName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public String getOauth2Token() {
        return oauth2Token;
    }

    public void setOauth2Token(String oauth2Token) {
        this.oauth2Token = oauth2Token;
    }

    @Override
    public String toString() {
        return "TodoApp{" +
                "id=" + id +
                ", oauth2Token='" + oauth2Token + '\'' +
                ", todoAppName='" + todoAppName + '\'' +
                '}';
    }
}
