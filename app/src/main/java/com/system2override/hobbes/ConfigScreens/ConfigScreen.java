package com.system2override.hobbes.ConfigScreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.system2override.hobbes.ConfigScreens.BannedAppManagement.BannedAppScreen;
import com.system2override.hobbes.HobbesScreen;
import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.R;

public class ConfigScreen extends HobbesScreen implements View.OnClickListener {
    Class nextClass;
    View next;
    View back;
    final ConfigScreen thisInstance = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        next = findViewById(R.id.nextButton);
        back = findViewById(R.id.backButton);

        if (MyApplication.inTutorial()) {
            next.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            next.setOnClickListener(this);
            back.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // close current Activity to go back
                    finish();
                }

            });
        } else {
            next.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, BannedAppScreen.class);

        startActivity(i);
    }

    private ConfigScreen returnThisInstance () {
        return thisInstance;
    }
}
