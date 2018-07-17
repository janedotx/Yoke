package com.system2override.yoke.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Entity(tableName = "Habits")
public class Habit {
    @PrimaryKey(autoGenerate = true)
    public int id;

    // milliseconds since unix epoch
    @ColumnInfo(name="lastDateCompleted")
    public long lastDateCompleted;

    @ColumnInfo(name="description")
    public String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLastDateCompleted() {
        return lastDateCompleted;
    }

    public void setLastDateCompleted(long lastDateCompleted) {
        this.lastDateCompleted = lastDateCompleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean completedToday() {
        Calendar todayCalObj = new GregorianCalendar();
        todayCalObj.setTimeInMillis(System.currentTimeMillis());

        Calendar lastDateCompletedTimeObj = new GregorianCalendar();
        lastDateCompletedTimeObj.setTimeInMillis(this.lastDateCompleted);

        return (todayCalObj.get(Calendar.YEAR) == lastDateCompletedTimeObj.get(Calendar.YEAR) &&
                todayCalObj.get(Calendar.DAY_OF_YEAR) == lastDateCompletedTimeObj.get(Calendar.DAY_OF_YEAR));
    }
}
