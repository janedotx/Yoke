package com.system2override.hobbes.OnboardingScreens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.provider.Settings;

import com.system2override.hobbes.R;

public class PrivacyScreen extends OnboardingActivity implements View.OnClickListener {
    private static final String TAG = "PrivacyScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.onboardingTextHeaderString = "HOBBES IS COMPLETELY PRIVATE";
        this.onboardingTextBodyString = "Your data never leaves your phone. But we do need access to your usage settings permission to help monitor your app usage.";
        this.progressDotID = R.id.thirdProgressDot;
        this.drawableId = R.drawable.onboarding_grandpa_3;
        super.onCreate(savedInstanceState);

        this.next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (hasNoUsageStatsPermission(this)) {
            Log.d(TAG, "onClick: has no usage stats permission");
            startActivity( new Intent(this, AfterHowToScreen.class));
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        } else {
            Log.d(TAG, "onClick: has usage stats permission");
            startActivity( new Intent(this, AfterHowToScreen.class));
        }

    }

}
