package com.system2override.hobbes.OttoMessages;

import android.util.Log;

import com.system2override.hobbes.Models.ToDoInterface;

public class ToDoCompletedEvent {
    private static final String TAG = "ToDoCompletedEvent";
    public ToDoInterface toDo;
    public ToDoCompletedEvent(ToDoInterface toDo) {
        this.toDo = toDo;
        Log.d(TAG, "ToDoCompletedEvent: created");
    }
}
