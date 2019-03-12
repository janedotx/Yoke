package com.system2override.hobbes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.system2override.hobbes.ConfigScreens.BannedAppManagement.BannedAppScreen;

public class HasBottomNavScreen extends HobbesScreen implements View.OnClickListener {
    private static final String TAG = "HasBottomNavScreen";
    public Class nextClass;
    public View bottomNavButtons;
    public View next;
    public View back;
    final HasBottomNavScreen thisInstance = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        bottomNavButtons = findViewById(R.id.bottomNavButtons);
        next = findViewById(R.id.nextButton);
        back = findViewById(R.id.backButton);

        if (MyApplication.inTutorial()) {
            bottomNavButtons.setVisibility(View.VISIBLE);
            next.setOnClickListener(this);
            back.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // close current Activity to go back
                    finish();
                }

            });
        } else {
            bottomNavButtons.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, BannedAppScreen.class);

        startActivity(i);
    }

}
