package com.system2override.hobbes;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.system2override.hobbes.UsageHistory.UsageHistoryScreen;

import org.junit.After;

public class AfterHowToScreen extends OnboardingActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.textView.setText("Now that you granted Hobbes permission to access usage stats, he will show you what your current phone usage habits are. After that, you will configure how much time you want to start each day with, and which apps you want to limit time on. Finally, you will create some todos.\n\nThere are two kinds of todos: daily habits, and one-off tasks.");
        this.next.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(AfterHowToScreen.this, UsageHistoryScreen.class));
                                    }
                                }
        );
    }

}
