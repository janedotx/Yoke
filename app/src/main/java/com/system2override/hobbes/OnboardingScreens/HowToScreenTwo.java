package com.system2override.hobbes.OnboardingScreens;

import android.view.View;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;

public class HowToScreenTwo  extends OnboardingActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.next.setOnClickListener(this);
        this.onboardingTextHeader.setText("EARN TIME BY COMPLETING TODOS");
        this.onboardingTextBody.setText("Whenever you finish a todo, you'll earn more time.");

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, HowToScreenThree.class));
    }
}
