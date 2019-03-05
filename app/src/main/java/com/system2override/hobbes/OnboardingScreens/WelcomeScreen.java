package com.system2override.hobbes.OnboardingScreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.system2override.hobbes.OnboardingScreens.HowToScreen;
import com.system2override.hobbes.OnboardingScreens.OnboardingActivity;

public class WelcomeScreen extends OnboardingActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.next.setOnClickListener(this);
        this.onboardingTextHeader.setText("WELCOME");
        this.onboardingTextBody.setText("Hobbes is a todo list that will both help you control your phone usage, and get more done in the real world.");
        this.bar.setDisplayHomeAsUpEnabled(false);

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, HowToScreen.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
