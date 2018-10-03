package com.system2override.hobbes.OttoMessages;

import com.system2override.hobbes.Models.ToDoInterface;

public class ToDoCreated {
    public ToDoInterface todo;
    public ToDoCreated(ToDoInterface todo) {
        this.todo = todo;
    }
}
