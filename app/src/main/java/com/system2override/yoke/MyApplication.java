package com.system2override.yoke;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        File path = new File(this.getExternalFilesDir(null), "logcat.log");
        Log.d(TAG, "onCreate: path " + path.toString());
//        writeLogCat();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    // bad to have this running while debugging, because logcat no longer prints to stdout and you can't
    // see it in Android Studio
    public void writeLogCat() {
            File path = new File(this.getExternalFilesDir(null), "logcat.log");
        Log.d(TAG, "writeLogCat: PATH " + path);
            try {
                Process process = Runtime.getRuntime().exec("logcat -c");
                process = Runtime.getRuntime().exec("logcat -r 10000 -f " + path.getAbsolutePath());
            } catch ( IOException e ) {
                e.printStackTrace();
            }
    }
}
