package com.system2override.yoke.Models.RoomModels;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Suggestions")
public class Suggestion {
    // eventually stick array here to link types to something more human-readable
    // or at least some comments here to indicate what number goes with what human-readable question
    // type
    private static final String TAG = "Suggestion";

    @PrimaryKey(autoGenerate = true)
    public int id;

    // The text of the suggestions
    public String text;

    // Did the user use them
    public boolean used = false;

    public int type = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
