package com.system2override.hobbes;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.system2override.hobbes.TodoManagement.TodoManagementScreen;

public class SplashScreen extends HobbesScreen implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        View getStarted = findViewById(R.id.getStarted);
        getStarted.setOnClickListener(this);

        /*
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent mainIntent = new Intent(SplashScreen.this, WelcomeScreen.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, 2500);
//        */

    }

    @Override
    public void onClick(View v) {
        finish();
        startActivity(new Intent(SplashScreen.this, WelcomeScreen.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
