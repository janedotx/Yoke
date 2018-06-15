package com.system2override.yoke.models;

import android.arch.persistence.room.TypeConverter;

public class MyTypeConverters {
    @TypeConverter
    public static boolean fromInt(int i) {
        if (i == 0) { return false; }
        return true;
    }

    @TypeConverter
    public static int fromBoolean(boolean b) {
        if (b) { return 1; }
        return 0;
    }

}
