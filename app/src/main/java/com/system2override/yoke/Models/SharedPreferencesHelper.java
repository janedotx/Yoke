package com.system2override.yoke.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.system2override.yoke.MyApplication;

public class SharedPreferencesHelper {
    String mFullFilePath;
    public SharedPreferencesHelper(String file) {
        this.mFullFilePath = MyApplication.packageName + "." + file;
    }

    public SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(mFullFilePath, Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        return context.getSharedPreferences(mFullFilePath, Context.MODE_PRIVATE).edit();
    }
}
