package com.system2override.hobbes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.system2override.hobbes.ConfigScreens.BannedAppManagement.BannedAppScreen;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

public class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "DefaultUncaughtExceptio";
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;
    private Context context;

    public DefaultUncaughtExceptionHandler(Thread.UncaughtExceptionHandler pDefaultExceptionHandler, Context context)
    {
        defaultExceptionHandler = pDefaultExceptionHandler;
        this.context = context;
    }

    public void uncaughtException(Thread t, Throwable e) {
        Log.d(TAG, "uncaughtException: " + "caught me");
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        try {
            FileOutputStream stream = context.openFileOutput("error.log", Context.MODE_APPEND);
            stream.write(new Date().toString().getBytes());
            stream.write("\n".getBytes());
            stream.write(result.toString().getBytes());
            stream.flush();
            stream.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        defaultExceptionHandler.uncaughtException(t, e);
    }

}
