package com.system2override.hobbes.OnboardingScreens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.system2override.hobbes.OnboardingScreens.HowToScreen;
import com.system2override.hobbes.OnboardingScreens.OnboardingActivity;
import com.system2override.hobbes.R;

public class WelcomeScreen extends OnboardingActivity implements View.OnClickListener {
    private static final String TAG = "WelcomeScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.onboardingTextHeaderString = "WELCOME.\nNEVER FORGET A TODO AGAIN";
        this.onboardingTextBodyString = "Hobbes is a todo list that will both help you control your phone usage, and get more done in the real world.";
        this.drawableId = R.drawable.onboarding_grandpa_1;
        Log.d(TAG, "onCreate: ");
        Log.d(TAG, "onCreate: about to super.onCreate for OnboardingActivity");
        super.onCreate(savedInstanceState);

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
