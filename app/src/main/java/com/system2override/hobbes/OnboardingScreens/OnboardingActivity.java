package com.system2override.hobbes.OnboardingScreens;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.system2override.hobbes.HasBottomNavScreen;
import com.system2override.hobbes.HobbesScreen;
import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.R;

import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.support.v4.app.AppOpsManagerCompat.MODE_ALLOWED;

public class OnboardingActivity extends HasBottomNavScreen {
    private static final String TAG = "OnboardingActivity";
    protected ActionBar bar;
    protected TextView onboardingTextHeaderView;
    protected TextView onboardingTextBodyView;
    protected String onboardingTextHeaderString;
    protected String onboardingTextBodyString;
    protected int progressDotID;
    protected int drawableId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.onboarding_screen);
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.bar =  getSupportActionBar();
        this.bar.setTitle("Hobbes");
        this.bar.setDisplayHomeAsUpEnabled(true);

        this.onboardingTextHeaderView = (TextView) findViewById(R.id.onboardingTextHeader);
        this.onboardingTextBodyView = (TextView) findViewById(R.id.onboardingTextBody);
        this.onboardingTextHeaderView.setText(this.onboardingTextHeaderString);
        this.onboardingTextBodyView.setText(this.onboardingTextBodyString);
        ((ImageView) findViewById(progressDotID)).setImageResource(R.drawable.brown_progress_dot);
        ((ImageView) findViewById(R.id.onboardingImage)).setImageResource(drawableId);
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

    public boolean hasNoUsageStatsPermission(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo appInfo = packageManager.getApplicationInfo(MyApplication.packageName, 0);
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, appInfo.uid, context.getPackageName());
            return mode != MODE_ALLOWED;
        }  catch (PackageManager.NameNotFoundException e ) {
            Log.d(TAG, "checkForUsageStatsPermission: oops wrong package name");
            return false;
        }
    }



}
