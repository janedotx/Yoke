package com.system2override.yoke;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

// though i dont need indices, as i anticipate there being like five todo apps max
@Entity(tableName="TodoRules",
        foreignKeys = {@ForeignKey(entity = TodoApp.class,
                                            parentColumns = "id",
                                            childColumns="todoappId",
                                            onDelete = CASCADE)},
        indices = {@Index(name = "todoApp_index", value = "todoappId"),
                    @Index(value = "packageName")}
        )
public class TodoRule extends Rule {
    @android.support.annotation.NonNull
    @ColumnInfo(name="packageName")
    private String packageName;

    @ColumnInfo(name="time")
    private int time;

    @ColumnInfo(name = "todoappId")
    private int todoappId;

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

    public int getTodoappId() {
        return todoappId;
    }

    public void setTodoappId(int todoappId) {
        this.todoappId = todoappId;
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
        return "TodoRule{" +
                "id=" + this.id +
                ", packageName='" + packageName + '\'' +
                ", time=" + time +
                ", todoappId=" + todoappId +
                '}';
    }
}
