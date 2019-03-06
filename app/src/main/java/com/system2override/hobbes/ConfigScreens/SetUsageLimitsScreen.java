package com.system2override.hobbes.ConfigScreens;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;
import com.system2override.hobbes.ConfigScreens.BannedAppManagement.BannedAppScreen;
import com.system2override.hobbes.DefaultUncaughtExceptionHandler;
import com.system2override.hobbes.Models.TimeBank;
import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.R;

public class SetUsageLimitsScreen extends DefaultUncaughtExceptionHandler.ConfigScreen {
    ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_usage_limits_screen);
        nextClass = BannedAppScreen.class;

        super.onCreate(savedInstanceState);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.bar = getSupportActionBar();
        this.bar.setTitle(R.string.set_phone_usage_time_limit);
        this.bar.setDisplayHomeAsUpEnabled(true);
        TimeBank timeBank = MyApplication.getTimeBank();


        /*
        if (MyApplication.inTutorial()) {
            next.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(thisInstance, SetUsageLimitsScreen.this.nextClass);
                    startActivity(i);
                }
            });
            back.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // close current Activity to go back
                    finish();
                }

            });
        } else {
            next.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
        }
        */

//        setUsageLimitMinutes = findViewById(R.id.setUsageLimitMinutes);
//        SeekBar seekBar = findViewById(R.id.setInitialTimeSeekBar);

        HoloCircleSeekBar holoCircleSeekBar = (HoloCircleSeekBar) findViewById(R.id.picker);
        holoCircleSeekBar.getValue();

        int progress = (int) timeBank.getInitialTime()/ (60000);
        holoCircleSeekBar.setValue(progress);
        holoCircleSeekBar.setOnSeekBarChangeListener(new HoloCircleSeekBar.OnCircleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(HoloCircleSeekBar seekBar, int progress, boolean fromUser) {
                MyApplication.getTimeBank().setInitialTime(progress * 60 * 1000);
            }

            @Override
            public void onStartTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) { }

            @Override
            public void onStopTrackingTouch(HoloCircleSeekBar seekBar) {
                int progress = seekBar.getValue();
                MyApplication.getTimeBank().setInitialTime(progress * 60 * 1000);
            }

        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
