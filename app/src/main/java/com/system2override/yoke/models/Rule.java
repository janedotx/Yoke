package com.system2override.yoke.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.SharedPreferences;

@Entity
public class Rule {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String getCounterName() {
        return this.getClass().getName() + Integer.toString(this.id);
    }

    public boolean isRuleBroken(SharedPreferences sharedPrefs) {

        return true;
    }

    public void incrementTimer(SharedPreferences sharedPrefs) {

    }

}
