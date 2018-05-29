package com.system2override.yoke;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

// because few apps are naughty
@Entity(tableName="Apps")
public class App {
    @ColumnInfo(name="packageName")
    public String packageName;

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="isNaughty")
    public boolean isNaughty;
}
