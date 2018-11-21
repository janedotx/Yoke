package com.system2override.hobbes;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.system2override.hobbes.BannedAppManagement.BannedAppScreen;
import com.system2override.hobbes.Models.TimeBank;

import java.util.Set;

public class SetUsageLimitsScreen extends AppCompatActivity {

    TextView setUsageLimitMinutes;
    ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_usage_limits_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.bar = getSupportActionBar();
        this.bar.setTitle(R.string.set_phone_usage_time_limit);
        this.bar.setDisplayHomeAsUpEnabled(true);
        TimeBank timeBank = MyApplication.getTimeBank();


        View next = findViewById(R.id.nextBar);
        if (MyApplication.inTutorial()) {
            next.setVisibility(View.VISIBLE);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SetUsageLimitsScreen.this, BannedAppScreen.class);
                    startActivity(i);
                }
            });
        } else {
            next.setVisibility(View.GONE);
        }

        setUsageLimitMinutes = findViewById(R.id.setUsageLimitMinutes);
        SeekBar seekBar = findViewById(R.id.setInitialTimeSeekBar);

        int progress = (int) timeBank.getInitialTime()/ (60000);
        seekBar.setProgress(progress);
        setUsageLimitMinutes.setText(Integer.toString(progress));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                MyApplication.getTimeBank().setInitialTime(progress * 60 * 1000);
                setUsageLimitMinutes.setText(Integer.toString(progress));
            }
        });
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
