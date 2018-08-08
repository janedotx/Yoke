package com.system2override.yoke.AppLimit;

import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.RoomModels.LocalTask;
import com.system2override.yoke.Models.ToDoInterface;

import java.util.ArrayList;
import java.util.List;

public class AppLimitTasks {
    HarnessDatabase db;

    public AppLimitTasks(HarnessDatabase db) {
        this.db = db;
    }

    public List<ToDoInterface> getToDos() {
        List<ToDoInterface> toDos = new ArrayList<>();
        List<Habit> habits = this.db.habitDao().getAllHabitsBefore(Habit.convertMSToYYMMDD(System.currentTimeMillis()), 3);
        List<LocalTask> localTasks = new ArrayList<>();
        if (habits.size() < 3) {
            localTasks = this.db.localTaskDao().getSomeIncompletedTasks(3 - habits.size());
        }

        for (Habit h: habits) {
            toDos.add(h);
        }

        for (LocalTask l: localTasks) {
            toDos.add(l);
        }

        return toDos;
    }
}
