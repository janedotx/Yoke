package com.system2override.yoke.Models;

import com.system2override.yoke.HarnessDatabase;

public interface ToDoInterface {
    public String getDescription();
    public String getToDoType();
    public boolean isCompleted();
    public int getId();
    public void setCompleted(boolean b);
    public void save(HarnessDatabase db);

}
