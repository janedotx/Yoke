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

import com.system2override.yoke.MyApplication;
import com.system2override.yoke.R;
import com.system2override.yoke.Utilities.UsageStatsHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class BannedAppScreen extends AppCompatActivity {
    private static final String TAG = "BannedAppScreen";
    private ShowAppsAdapter showAppsAdapter;
    private SelectedAppIconAdapter selectedAppIconAdapter;
    private RecyclerView showAppsRecyclerView;
    private RecyclerView showSelectedAppIconsView;
    private ActionBar bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bar = getSupportActionBar();
        this.bar.setTitle(R.string.banned_app_screen_bar_header);
        this.bar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_banned_apps_screen);

        this.showSelectedAppIconsView = (RecyclerView) findViewById(R.id.bannedAppsIcons);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.showSelectedAppIconsView.setLayoutManager(layoutManager);
        this.selectedAppIconAdapter = new SelectedAppIconAdapter(this,
                MyApplication.getBannedApps().getApplicationInfoObjectsWithNullPadding(),
                MyApplication.getBus()
        );
        this.showSelectedAppIconsView.setAdapter(this.selectedAppIconAdapter);

        List<ApplicationInfo> applicationInfoList = getApplicationList();
        List<String> appStrings = getAppStrings(applicationInfoList);

        this.showAppsRecyclerView = (RecyclerView) findViewById(R.id.chooseAppsRecyclerView);
        this.showAppsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.showAppsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.showAppsAdapter = new ShowAppsAdapter(this, applicationInfoList, MyApplication.getBus());
        this.showAppsRecyclerView.setAdapter(this.showAppsAdapter);

/*        List<String> apps = UsageStatsHelper.getAppsByTotalTime(this, 10, 1000 * 60 * 60 * 24 * 7, appStrings);
        final PackageManager pm = getApplicationContext().getPackageManager();
        for (String s: apps) {
            ApplicationInfo ai;
            try {
                ai = pm.getApplicationInfo( s, 0);
            } catch (final PackageManager.NameNotFoundException e) {
                ai = null;
            }
            final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
            Log.d(TAG, "onCreate: app " + applicationName + " " + s);
        }
        */
        Map<Long, String> map = UsageStatsHelper.sortAppsByTime(UsageStatsHelper.getAppsByTotalTime(this, 1000 * 60 * 60 * 24 * 7));
        for (Long key: map.keySet()) {
            Log.d(TAG, "onCreate: "+ map.get(key).toString() + " " +  key );
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected: id  is " + Integer.toString(id));
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private List<String> getAppStrings(List<ApplicationInfo> applicationInfoList) {
        List<String> strings = new ArrayList<>();
        for (ApplicationInfo applicationInfo: applicationInfoList) {
            strings.add(applicationInfo.packageName);
        }
        return strings;

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
