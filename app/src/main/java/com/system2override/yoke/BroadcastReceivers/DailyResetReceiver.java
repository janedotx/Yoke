package com.system2override.yoke.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.system2override.yoke.TimeBank;

public class DailyResetReceiver extends BroadcastReceiver{
    private static final String TAG = "DailyResetReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        if (TimeBank.RESET_ACTION.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: doing a reset now");
            TimeBank.resetTime(context);
            Toast.makeText(context, "Your toast message",
                    Toast.LENGTH_LONG).show();
        }
    }
}
