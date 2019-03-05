package com.system2override.hobbes.OnboardingScreens;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.provider.Settings;

public class HowToScreenThree  extends OnboardingActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.next.setOnClickListener(this);
        this.onboardingTextHeader.setText("HOBBES IS COMPLETELY PRIVATE");
        this.onboardingTextBody.setText("Your data never leaves your phone. But we do need access to your usage settings permission to help monitor your app usage.");

    }

    @Override
    public void onClick(View v) {
        if (hasNoUsageStatsPermission(this)) {
            startActivity( new Intent(this, AfterHowToScreen.class));
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        } else {
            startActivity( new Intent(this, AfterHowToScreen.class));
        }

    }

}
