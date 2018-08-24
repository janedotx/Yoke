package com.system2override.yoke.OttoMessages;

import com.system2override.yoke.Models.ToDoInterface;

public class ToDoCompletedEvent {
    public ToDoInterface toDo;
    public ToDoCompletedEvent(ToDoInterface toDo) {
        this.toDo = toDo;
    }
}
