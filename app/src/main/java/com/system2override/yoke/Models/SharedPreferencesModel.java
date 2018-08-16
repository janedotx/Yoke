package com.system2override.yoke.Models;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesModel {

    public SharedPreferencesHelper helper;
    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;
    public Context context;

    public String FILE;

    public SharedPreferencesModel(Context c) {
        this.context = c;
        this.prefs = getSharedPreferencesHelper().getSharedPreferences(this.context);
        this.editor = getSharedPreferencesHelper().getSharedPreferencesEditor(this.context);
    }

    public SharedPreferencesHelper getSharedPreferencesHelper(){
        if (helper == null) {
            helper = new SharedPreferencesHelper(FILE);
        }
        return helper;
    }
}
