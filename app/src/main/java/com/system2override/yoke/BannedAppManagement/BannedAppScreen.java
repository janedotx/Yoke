package com.system2override.yoke.BannedAppManagement;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.system2override.yoke.MainActivity;
import com.system2override.yoke.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BannedAppScreen extends AppCompatActivity {
    private static final String TAG = "BannedAppScreen";
    private BannedAppAdapter adapter;
    private RecyclerView recyclerView;
    private ActionBar bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bar = getSupportActionBar();
        this.bar.setTitle(R.string.banned_app_screen_bar_header);

        setContentView(R.layout.activity_banned_apps_screen);
        List<ApplicationInfo> applicationInfoList = getApplicationList();
        this.recyclerView = (RecyclerView) findViewById(R.id.chooseAppsRecyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.adapter = new BannedAppAdapter(this, applicationInfoList);
        this.recyclerView.setAdapter(this.adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected: id  is " + Integer.toString(id));
        if (id == android.R.id.home) {
            Log.d(TAG, "onOptionsItemSelected: home pressed");
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private List<ApplicationInfo> getApplicationList() {
        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfosList = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);
        List<ApplicationInfo> applicationInfoList = new ArrayList<>();
        // maybe this is too defensive but i don't wanna nab any Service by accident
        for (int i = 0; i < resolveInfosList.size(); i ++) {
            ResolveInfo cur = resolveInfosList.get(i);
            if (cur.activityInfo != null) {
                try {
                    ApplicationInfo appInfo = pm.getApplicationInfo(cur.activityInfo.packageName, 0);
                    applicationInfoList.add(appInfo);
                    Log.d(TAG, "onBindViewHolder: packageName " + cur.activityInfo.packageName);
                    Log.d(TAG, "onBindViewHolder: application label " + pm.getApplicationLabel(appInfo));
                } catch (PackageManager.NameNotFoundException e) {
                    continue;
                }
            }
        }

        return sortApplicatioInfoList(applicationInfoList);
    }

    class ApplicationInfoComparator implements Comparator<ApplicationInfo> {

        @Override
        public int compare(ApplicationInfo first, ApplicationInfo second) {
            PackageManager pm = BannedAppScreen.this.getPackageManager();
            String applicationLabelOne = (String) pm.getApplicationLabel(first);
            String applicationLabelTwo = (String) pm.getApplicationLabel(second);
            return applicationLabelOne.compareTo(applicationLabelTwo);

        }
    }

    private List<ApplicationInfo> sortApplicatioInfoList(List<ApplicationInfo> applicationInfoList) {
        Collections.sort(applicationInfoList, new ApplicationInfoComparator());
        return applicationInfoList;
    }
}
