package com.system2override.hobbes.OttoMessages;

import com.system2override.hobbes.Models.ToDoInterface;

public class ToDoCompletedEvent {
    public ToDoInterface toDo;
    public ToDoCompletedEvent(ToDoInterface toDo) {
        this.toDo = toDo;
    }
}
