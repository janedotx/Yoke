package com.system2override.yoke.OttoMessages;

import com.system2override.yoke.Models.ToDoInterface;

public class ToDoUncheckedEvent {
    public ToDoInterface toDo;
    public ToDoUncheckedEvent(ToDoInterface toDo) {
        this.toDo = toDo;
    }
}
