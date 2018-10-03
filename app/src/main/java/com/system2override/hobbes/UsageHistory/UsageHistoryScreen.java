package com.system2override.hobbes.UsageHistory;

import android.app.usage.UsageStatsManager;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.system2override.hobbes.Models.BeforeHobbesAppData;
import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.R;
import com.system2override.hobbes.Utilities.RandomUtilities;
import com.system2override.hobbes.Utilities.UsageStatsHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UsageHistoryScreen extends AppCompatActivity {
    private final int INSUFFICIENT_TIME = 1;
    private final int LESS_THAN_A_WEEK = 2;
    private final int A_WEEK_PLUS = 3;
    private RecyclerView recyclerView;
    private AppsUsageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_history_screen);

        if (MyApplication.getOneTimeData().firstInstallIncomplete()) {
            setBeforeHobbesUsage();
        }
        List<ApplicationInfo> applicationInfoList = RandomUtilities.getApplicationList(getPackageManager());
        Map<String, ApplicationInfo> applicationInfoMap = new HashMap<>();
        for (ApplicationInfo applicationInfo: applicationInfoList) {
            applicationInfoMap.put(applicationInfo.packageName, applicationInfo);
        }

        List<Map.Entry<Long, String>> appsTimeMap = UsageStatsHelper.convertSortedMapToList(
                UsageStatsHelper.sortAppsByTime(
                        UsageStatsHelper.getAppsByTotalTime(this, UsageStatsHelper.WEEK_IN_MS)));


        this.recyclerView = (RecyclerView) findViewById(R.id.appUsageHistory);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());

        this.adapter = new AppsUsageAdapter(this, appsTimeMap, applicationInfoMap);
        this.recyclerView.setAdapter(adapter);
    }


    private boolean canGetAverageAfterHobbes() {
        long timeSinceInstall = MyApplication.getOneTimeData().getTimeOfHobbesInstall();
        if (timeSinceInstall < UsageStatsHelper.DAY_IN_MS) {
            return false;
        } else {
            return true;
        }
    }

    private void setAfterHobbesUsage() {
        if (canGetAverageAfterHobbes()) {

        }
    }

    private void setBeforeHobbesUsage() {
        Map<String, Long> map = UsageStatsHelper.getAppsByTotalTime(this, UsageStatsHelper.WEEK_IN_MS);

        long totalTime = UsageStatsHelper.sumTotalTimeOverInterval(map);
        MyApplication.getOneTimeData().setAverageDailyUsageOverall(totalTime/7);

        BeforeHobbesAppData beforeHobbesAppData = MyApplication.getBeforeHobbesAppData();

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry pair = (Map.Entry) iter.next();
            beforeHobbesAppData.setTimeForApp((String) pair.getKey(), ((Long)pair.getValue()).longValue());
        }
    }
}
