package com.system2override.hobbes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.system2override.hobbes.OnboardingScreens.WelcomeScreen;

public class SplashScreen extends HobbesScreen implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        View getStarted = findViewById(R.id.getStarted);
        getStarted.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(SplashScreen.this, WelcomeScreen.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
