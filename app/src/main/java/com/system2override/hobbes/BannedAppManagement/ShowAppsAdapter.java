package com.system2override.hobbes.BannedAppManagement;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.system2override.hobbes.Models.BannedApps;
import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.OttoMessages.BannedAppAdded;
import com.system2override.hobbes.OttoMessages.BannedAppRemoved;
import com.system2override.hobbes.R;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShowAppsAdapter extends RecyclerView.Adapter<ShowAppsViewHolder>{
    private static final String TAG = "ShowAppsAdapter";
    private Map<String, ApplicationInfo> applications;
    private PackageManager pm;
    private  List<Map.Entry<Long, String>> appsTimeList;
    private Context context;
    Bus bus;

    public ShowAppsAdapter(Context context,  List<Map.Entry<Long, String>> appsTimeList, Map<String, ApplicationInfo> applications, Bus bus) {
        this.applications = applications;
        this.appsTimeList = appsTimeList;
        this.context = context;
        this.pm = this.context.getPackageManager();
        this.bus = bus;
        bus.register(this);
    }

    @NonNull
    @Override
    public ShowAppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_detail, parent, false);
        return new ShowAppsViewHolder(v, this.context, new ShowAppsViewHolder.BannedAppClickListener() {
            @Override
            public void onClick(View view, int position) {
                ShowAppsAdapter adapter = ShowAppsAdapter.this;
                MyApplication.getBannedApps().printBannedApps();;
                String appName = ShowAppsAdapter.this.applications.get(position).packageName;

                // unchecking case
                if (MyApplication.getBannedApps().getApps().contains(appName)) {
                    MyApplication.getBannedApps().removeApp(appName);
                    ((CheckBox)view.findViewById(R.id.singleAppCheckBox)).setChecked(false);
                    adapter.bus.post(new BannedAppRemoved(adapter.applications.get(position)));
                } else {
                    // checking case
                    if (MyApplication.getBannedApps().getApps().size() < BannedApps.BANNED_APPS_LIMIT) {
                        MyApplication.getBannedApps().addApp(appName);
                        ((CheckBox) view.findViewById(R.id.singleAppCheckBox)).setChecked(true);
                        adapter.bus.post(new BannedAppAdded(adapter.applications.get(position)));
                    } else {
                        ((CheckBox) view.findViewById(R.id.singleAppCheckBox)).setChecked(false);
                    }
                }
                MyApplication.getBannedApps().printBannedApps();;
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAppsViewHolder holder, int position) {
        String singleAppPackageName = this.appsTimeList.get(position).getValue();
        ApplicationInfo appInfo = this.applications.get(singleAppPackageName);

        Log.d(TAG, "onBindViewHolder: is this null? appInfo " + Boolean.toString(appInfo == null));
        if (appInfo != null) {
            holder.appText.setText(this.pm.getApplicationLabel(appInfo));
        } else {
            holder.appText.setText(singleAppPackageName);
        }

        try {
            holder.appImage.setImageDrawable(this.pm.getApplicationIcon(singleAppPackageName));
        } catch (PackageManager.NameNotFoundException e) {
            holder.appImage
                    .setImageDrawable(ResourcesCompat.getDrawable(this.context.getResources(), R.drawable.empty_app_icon, null));
        }

        if (MyApplication.getBannedApps().getApps().contains(singleAppPackageName)) {
            Log.d(TAG, "onBindViewHolder: banned apps contains " + singleAppPackageName);
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return this.appsTimeList.size();
    }

    @Subscribe
    public void onAppUnselected(BannedAppRemoved event) {
        notifyDataSetChanged();

    }

}

