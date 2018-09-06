package com.system2override.yoke.OttoMessages;

import com.system2override.yoke.Models.ToDoInterface;

public class ToDoCreated {
    public ToDoInterface todo;
    public ToDoCreated(ToDoInterface todo) {
        this.todo = todo;
    }
}
