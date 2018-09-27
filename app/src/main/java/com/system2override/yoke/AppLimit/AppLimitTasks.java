package com.system2override.yoke.AppLimit;

import com.system2override.yoke.HarnessDatabase;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.RoomModels.LocalTask;
import com.system2override.yoke.Models.ToDoInterface;

import java.util.ArrayList;
import java.util.List;

public class AppLimitTasks {
    public static final int NO_STREAK = 0;
    public static final int STREAK_COMPLETED = 1;
    public static final int ALL_COMPLETED = 2;

    HarnessDatabase db;
    private int type;
    private List<ToDoInterface> toDos;

    public AppLimitTasks(HarnessDatabase db) {
        this.db = db;
        this.toDos = calculateToDos();
    }

    public List<ToDoInterface> calculateToDos() {
        List<ToDoInterface> toDos = new ArrayList<>();
        List<Habit> habits = this.db.habitDao().loadNumIncompleteHabits(3);
        List<Habit> oneOffs = new ArrayList<>();
        if (habits.size() > 0) {
            this.type = NO_STREAK;
        }

        if (habits.size() == 0) {
            this.type = STREAK_COMPLETED;
        }

        if (habits.size() < 3) {
            oneOffs = this.db.habitDao().loadNumIncompleteOneOffs(3 - habits.size());
        }

        for (Habit h: habits) {
            toDos.add(h);
        }

        for (Habit h: oneOffs) {
            toDos.add(h);
        }

        if (toDos.size() == 0) {
            this.type = ALL_COMPLETED;
        }

        return toDos;
    }

    public int getType() {
        return type;
    }

    public List<ToDoInterface> getToDos() { return this.toDos; }
}
