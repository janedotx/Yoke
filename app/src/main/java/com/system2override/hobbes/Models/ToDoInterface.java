package com.system2override.hobbes.Models;

import com.system2override.hobbes.HarnessDatabase;

public interface ToDoInterface {
    public String getDescription();
    public String getToDoType();
    public boolean isCompleted();
    public int getId();
    public void setCompleted(boolean b);
    public void save(HarnessDatabase db);
    public boolean getIsDailyHabit();

}
