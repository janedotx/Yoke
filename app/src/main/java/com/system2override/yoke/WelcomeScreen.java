package com.system2override.yoke;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WelcomeScreen extends AppCompatActivity implements View.OnClickListener {
    public ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bar = getSupportActionBar();
        this.bar.setTitle("Welcome");

        setContentView(R.layout.activity_welcome_screen);

        View next = findViewById(R.id.welcomeNextButton);
        next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, HowToScreen.class));
        finish();
    }
}
