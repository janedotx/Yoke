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
                                            childColumns="todoAppId",
                                            onDelete = CASCADE)},
        indices = {@Index(value = "todoAppId")}
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

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "todoAppName")
    public String todoAppName;

    // RFC 3339 (which is in UTC)
    @ColumnInfo(name="updatedAt")
    public String updatedAt;

    // this is the id of the task back in its home app
    @ColumnInfo(name="todoAppIdString")
    public String todoAppIdString;

    // the task list that this task belongs to
    @ColumnInfo(name="taskListId")
    public String taskListIdString;

    // RFC 3339 (which is in UTC)
    @ColumnInfo(name="dueDate")
    public String dueDate;

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

    public void setTodoAppId(Integer todoAppId) {
        this.todoAppId = todoAppId;
    }

    public String getTodoAppName() {
        return todoAppName;
    }

    public void setTodoAppName(String todoAppName) {
        this.todoAppName = todoAppName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTodoAppIdString() {
        return todoAppIdString;
    }

    public void setTodoAppIdString(String todoAppIdString) {
        this.todoAppIdString = todoAppIdString;
    }

    @Override
    public String toString() {
        return "LocalTask{" +
                "id=" + id +
                ", completed=" + completed +
                ", dateCompleted='" + dateCompleted + '\'' +
                ", todoAppId=" + todoAppId +
                ", description='" + description + '\'' +
                ", todoApp='" + todoAppName + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", todoAppIdString='" + todoAppIdString + '\'' +
                '}';
    }

    public boolean hasSameId(LocalTask otherTask) {
        return this.todoAppIdString.equals(otherTask.getTodoAppIdString());
    }

    public String getTaskListIdString() {
        return taskListIdString;
    }

    public void setTaskListIdString(String taskListIdString) {
        this.taskListIdString = taskListIdString;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
