package com.system2override.yoke.Models.RoomModels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.model.Task;
import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.Models.ToDoInterface;

@Entity(tableName = "LocalTasks")
public class LocalTask  {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="completed")
    public boolean completed;

    // RFC 3339 (which is in UTC)
    @ColumnInfo(name="dateCompleted")
    public String dateCompleted;

    @ColumnInfo(name = "description")
    public String description;

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

    @ColumnInfo(name="parentID")
    public String parentID;

    //ToDoInterface
    public int getId() {
        return id;
    }

    // ToDoInterface
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        if (completed) {
            long ms = System.currentTimeMillis();
            this.setDateCompleted(new DateTime(ms).toStringRfc3339());
        }
    }

    public String getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
    }


    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public String getToDoType() { return this.getClass().getSimpleName(); }

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
                ", description='" + description + '\'' +
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

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public void setUpdatedAtInMS(long ms) {
        this.updatedAt = new DateTime(ms).toStringRfc3339();
    }

    public LocalTask() {
        this.setCompleted(false);
    }

    public LocalTask(Task task, String taskListId) {

        this.setCompleted((task.getCompleted() != null));
        if (task.getCompleted() != null) {
            this.setDateCompleted(task.getCompleted().toStringRfc3339());
        }
        this.setUpdatedAt(task.getUpdated().toStringRfc3339());
        this.setDescription(task.getTitle());
        this.setTodoAppIdString(task.getId());
        if (task.getDue() != null) {
            this.setDueDate(task.getDue().toStringRfc3339());
        }
        if (task.getParent() != null) {
            this.setParentID(task.getParent());
        }

        this.setTaskListIdString(taskListId);

    }

    public void save(HarnessDatabase db) {
        db.localTaskDao().update(this);
    }
}
