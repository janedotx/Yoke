package com.system2override.hobbes.OnboardingScreens;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;


public class HowToScreen extends OnboardingActivity implements View.OnClickListener {
    private static final String TAG = "HowToScreen";
    private ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.next.setOnClickListener(this);
        this.onboardingTextHeader.setText("NEVER FORGET A TODO AGAIN");
        this.onboardingTextBody.setText("Whenever you run out of time on an addictive app, your todo list shows up.");

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, HowToScreenTwo.class));
    }
}
