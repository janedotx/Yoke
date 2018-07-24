package com.system2override.yoke.AppLimit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.system2override.yoke.R;

public class AppLimitScreen extends AppCompatActivity implements AppLimitScreenView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_limit_screen);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
