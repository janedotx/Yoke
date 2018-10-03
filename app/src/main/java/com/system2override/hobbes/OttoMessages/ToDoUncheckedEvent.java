package com.system2override.hobbes.OttoMessages;

import com.system2override.hobbes.Models.ToDoInterface;

public class ToDoUncheckedEvent {
    public ToDoInterface toDo;
    public ToDoUncheckedEvent(ToDoInterface toDo) {
        this.toDo = toDo;
    }
}
