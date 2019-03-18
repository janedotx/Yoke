package com.system2override.hobbes.OnboardingScreens;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;

import com.system2override.hobbes.R;


public class HowToScreen extends OnboardingActivity implements View.OnClickListener {
    private static final String TAG = "HowToScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.onboardingTextHeaderString = "EARN PHONE TIME BY COMPLETING TODOS";
        this.onboardingTextBodyString = "Whenever you run out of time on your phone, you'll have " +
                "to do something in the real world.";
        this.progressDotID = R.id.secondProgressDot;
        this.drawableId = R.drawable.onboarding_grandpa_2;
        super.onCreate(savedInstanceState);

        this.next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, PrivacyScreen.class));
    }
}
