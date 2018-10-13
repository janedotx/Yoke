package com.system2override.hobbes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HobbesScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter, R.anim.exit);

    }
}
