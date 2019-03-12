package com.system2override.hobbes.UsageHistory;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.system2override.hobbes.HasBottomNavScreen;
import com.system2override.hobbes.HobbesScreen;
import com.system2override.hobbes.Models.OneTimeData;
import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.R;
import com.system2override.hobbes.ConfigScreens.SetUsageLimitsScreen;
import com.system2override.hobbes.Utilities.RandomUtilities;
import com.system2override.hobbes.Utilities.UsageStatsHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsageHistoryScreen extends HasBottomNavScreen implements  View.OnClickListener {
    private static final String TAG = "UsageHistoryScreen";
    private final int INSUFFICIENT_TIME = 1;
    private final int LESS_THAN_A_WEEK = 2;
    private final int A_WEEK_PLUS = 3;
    private ActionBar bar;
    private float scale;
    private int topBarLength = 147;
    private RecyclerView recyclerView;
    private AppsUsageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nextClass = SetUsageLimitsScreen.class;
        setContentView(R.layout.activity_usage_history_screen);
        super.onCreate(savedInstanceState);
        this.scale = this.getResources().getDisplayMetrics().density;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.bar = getSupportActionBar();
        this.bar.setTitle("Usage history");
        this.bar.setDisplayHomeAsUpEnabled(true);

        if (MyApplication.getOneTimeData().getFirstInstallIncomplete()) {
            setBeforeHobbesUsage();
            MyApplication.getOneTimeData().setFirstInstallIncomplete(false);
            Log.d(TAG, "onCreate: setting before hobbes usage");
        }

        List<ApplicationInfo> applicationInfoList = RandomUtilities.getApplicationList(getPackageManager());
        Map<String, ApplicationInfo> applicationInfoMap = new HashMap<>();
        for (ApplicationInfo applicationInfo: applicationInfoList) {
            if (applicationInfo != null) {
                applicationInfoMap.put(applicationInfo.packageName, applicationInfo);
            }
        }

        List<Map.Entry<Long, String>> appsTimeMap = UsageStatsHelper.convertSortedMapToList(
                UsageStatsHelper.sortAppsByTime(
                        UsageStatsHelper.getAppsByTotalTime(this, UsageStatsHelper.WEEK_IN_MS, System.currentTimeMillis())));


        this.recyclerView = (RecyclerView) findViewById(R.id.appUsageHistory);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());

        this.adapter = new AppsUsageAdapter(this, appsTimeMap, applicationInfoMap);
        this.recyclerView.setAdapter(adapter);

        setUpTotalAverageTimeBox();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    private int getAppBarLength(long time) {
        int appBarLength = (int) (this.topBarLength * ((double) time / (double) UsageStatsHelper.TWELVE_HOURS_IN_MS));
        if (appBarLength > this.topBarLength) {
            appBarLength = this.topBarLength;
        }
        return appBarLength;
    }

    private void setLengthOfBar(long time, View v) {
        int appBarLength = getAppBarLength(time);

        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.width = (int) (appBarLength * scale + 0.5f);
        v.setLayoutParams(params);
    }

    private void setUpTotalAverageTimeBox() {
        TextView recText = findViewById(R.id.usageRecommendationText);
        TextView topText = findViewById(R.id.usageHistoryTopBoxText);
        TextView bottomText = findViewById(R.id.usageHistoryBottomBarText);
        if (canGetAverageAfterHobbes()) {
            recText.setVisibility(View.GONE);
            findViewById(R.id.usageHistoryBottom).setVisibility(View.VISIBLE);

            View topBar = findViewById(R.id.totalUsageHistoryTopBar);
            long interval = System.currentTimeMillis() - MyApplication.getOneTimeData().getTimeOfHobbesInstall();
            if (interval > UsageStatsHelper.WEEK_IN_MS) {
                interval = UsageStatsHelper.WEEK_IN_MS;
            }
            long averageTimeLastWeek = getAverageTotalTimeOver(interval,
                    System.currentTimeMillis(),
                    (int) ((double) interval / (double) UsageStatsHelper.DAY_IN_MS));
            topText.setText("Your average daily phone usage after installing Hobbes is " + RandomUtilities.formatMillisecondsToHHMM(averageTimeLastWeek));
            topText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            setLengthOfBar(averageTimeLastWeek, topBar);

            View bottomBar = findViewById(R.id.totalUsageHistoryBottomBar);
            long averageTimeBeforeHobbes = MyApplication.getOneTimeData().getAverageDailyUsageOverall();
            bottomText.setText("Your average daily phone usage before installing Hobbes was " + RandomUtilities.formatMillisecondsToHHMM(averageTimeBeforeHobbes));
            bottomText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            setLengthOfBar(averageTimeBeforeHobbes, bottomBar);

        } else {
            recText.setVisibility(View.VISIBLE);
            findViewById(R.id.usageHistoryBottom).setVisibility(View.GONE);

            View topBar = findViewById(R.id.totalUsageHistoryTopBar);
            long averageTimeBeforeHobbes = MyApplication.getOneTimeData().getAverageDailyUsageOverall();
            topText.setText("Your average daily phone usage is " + RandomUtilities.formatMillisecondsToHHMM(averageTimeBeforeHobbes));
            topText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            setLengthOfBar(averageTimeBeforeHobbes, topBar);

            recText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        }
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

    private boolean canGetAverageAfterHobbes() {
        long timeSinceInstall = MyApplication.getOneTimeData().getTimeOfHobbesInstall();
        if (System.currentTimeMillis() - timeSinceInstall < UsageStatsHelper.DAY_IN_MS) {
            return false;
        } else {
            return true;
        }
    }

    private long getAverageTotalTimeOver(long interval, long end, int base) {
        Map<String, Long> map = UsageStatsHelper.getAppsByTotalTime(this, interval, end);
        long totalTime = UsageStatsHelper.sumTotalTimeOverInterval(map);
        return totalTime/base;
    }

    private void setBeforeHobbesUsage() {
        OneTimeData oneTimeData = MyApplication.getOneTimeData();
        long installTime = oneTimeData.getTimeOfHobbesInstall();
        oneTimeData.setAverageDailyUsageOverall(getAverageTotalTimeOver(UsageStatsHelper.WEEK_IN_MS, installTime, 7));

    }
}
