package com.system2override.hobbes.OnboardingScreens;

import android.view.View;
import android.content.Intent;
import android.os.Bundle;

public class HowToScreenTwo  extends OnboardingActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, PrivacyScreen.class));
    }
}
