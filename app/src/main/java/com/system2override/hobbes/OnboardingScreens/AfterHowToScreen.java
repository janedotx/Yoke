package com.system2override.hobbes.OnboardingScreens;

import android.content.Intent;
import android.provider.Settings;
import android.os.Bundle;
import android.view.View;

import com.system2override.hobbes.R;
import com.system2override.hobbes.UsageHistory.UsageHistoryScreen;

public class AfterHowToScreen extends OnboardingActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.onboardingTextHeaderString = "LET'S CONFIGURE HOBBES";
        this.onboardingTextBodyString = "Set your daily usage limit, choose apps to limit your time on, and make some todos.";
        this.progressDotID = R.id.thirdProgressDot;
        super.onCreate(savedInstanceState);

        if (!hasNoUsageStatsPermission(this)) {
            this.next.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 startActivity(new Intent(AfterHowToScreen.this, UsageHistoryScreen.class));
                                             }
                                         }
            );
        } else {
            finish();
            startActivity( new Intent(this, AfterHowToScreen.class));
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

}
