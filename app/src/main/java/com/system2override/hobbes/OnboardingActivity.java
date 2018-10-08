package com.system2override.hobbes;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class OnboardingActivity extends AppCompatActivity {
    public ActionBar bar;
    public View next;
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_screen);
        this.bar = getSupportActionBar();
        this.bar.setDisplayHomeAsUpEnabled(true);

        this.next = findViewById(R.id.nextButton);
        this.textView = (TextView) findViewById(R.id.onboardingText);
        this.textView.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
