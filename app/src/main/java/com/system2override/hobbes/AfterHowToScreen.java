package com.system2override.hobbes;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.system2override.hobbes.UsageHistory.UsageHistoryScreen;

import org.junit.After;

public class AfterHowToScreen extends AppCompatActivity {

    private ActionBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_how_to_screen);

        this.bar = getSupportActionBar();
        this.bar.setDisplayHomeAsUpEnabled(true);

        View next = findViewById(R.id.welcomeNextButton);
        next.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(AfterHowToScreen.this, UsageHistoryScreen.class));
                                    }
                                }
        );
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
