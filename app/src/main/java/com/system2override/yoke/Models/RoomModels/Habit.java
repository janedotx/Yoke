package com.system2override.yoke.Models.RoomModels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.Models.ToDoInterface;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Entity(tableName = "Habits")
public class Habit implements ToDoInterface {
    private static final String TAG = "Habit";
    @PrimaryKey(autoGenerate = true)
    public int id;

    // milliseconds since unix epoch
    @ColumnInfo(name="lastDateCompleted")
    public String lastDateCompleted = "";

    @ColumnInfo(name="description")
    public String description;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastDateCompleted() {
        return lastDateCompleted;
    }

    public void setLastDateCompleted(String lastDateCompleted) {
        this.lastDateCompleted = lastDateCompleted;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] splitDate() {
        return this.lastDateCompleted.split("-");
    }

    public boolean completedOn(Calendar calObj) {
        if (!this.lastDateCompleted.equals("")) {
            String [] splitDate = splitDate();
            return (calObj.get(Calendar.YEAR) == Integer.parseInt(splitDate[0]) &&
                    calObj.get(Calendar.MONTH) == Integer.parseInt(splitDate[1]) &&
                    calObj.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(splitDate[2]));
        } else {
            return false;
        }
    }

    @Override // ToDoInterface
    public boolean isCompleted() {
        Calendar calObj = new GregorianCalendar();
        calObj.setTimeInMillis(System.currentTimeMillis());
        return completedOn(calObj);
    }

    public void setCompleted(boolean completed) {
        Log.d(TAG, "setCompleted: ");
        if (completed) {
            this.setLastDateCompleted(Habit.convertMSToYYMMDD(System.currentTimeMillis()));
        } else {
            this.setLastDateCompleted("");
        }
    }

    @Override
    public String toString() {
        return Integer.toString(getId()) + " " + description + " " + Boolean.toString(isCompleted());
    }

    //ToDoInterface
    @Override
    public String getToDoType() { return this.getClass().getSimpleName(); }

    public static String convertMSToYYMMDD(long ms) {
        Calendar todayCalObj = new GregorianCalendar();
        todayCalObj.setTimeInMillis(ms);
        String year = Integer.toString(todayCalObj.get(Calendar.YEAR));
        String month = Integer.toString(todayCalObj.get(Calendar.MONTH));
        String day = Integer.toString(todayCalObj.get(Calendar.DAY_OF_MONTH));
        return year + "-" + month + "-" + day;
    }

    public void save(HarnessDatabase db) {
        Log.d(TAG, "save: and completed is " + Boolean.toString(this.isCompleted()));
        db.habitDao().update(this);
    }


}
