package com.system2override.yoke.BannedAppManagement;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.OttoMessages.BannedAppRemoved;
import com.system2override.yoke.R;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ShowAppsAdapter extends RecyclerView.Adapter<ShowAppsViewHolder>{
    private static final String TAG = "ShowAppsAdapter";
    private List<ApplicationInfo> applications;
    private Context context;
    Bus bus;

    public ShowAppsAdapter(Context context, List<ApplicationInfo> list, Bus bus) {
        this.applications = list;
        this.context = context;
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
                Log.d(TAG, "onClick: ");
                MyApplication.getBannedApps().printBannedApps();;
                String appName = ShowAppsAdapter.this.applications.get(position).packageName;
                Log.d(TAG, "onClick: appName clicked " + appName);

                if (MyApplication.getBannedApps().getApps().contains(appName)) {
                    MyApplication.getBannedApps().removeApp(appName);
                    ((CheckBox)view.findViewById(R.id.singleAppCheckBox)).setChecked(false);
                } else {
                    if (MyApplication.getBannedApps().getApps().size() < MyApplication.BANNED_APPS_LIMIT) {
                        MyApplication.getBannedApps().addApp(appName);
                        ((CheckBox) view.findViewById(R.id.singleAppCheckBox)).setChecked(true);
                    }
                }
                MyApplication.getBannedApps().printBannedApps();;
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAppsViewHolder holder, int position) {
        ApplicationInfo app = this.applications.get(position);
        ApplicationInfo appInfo = this.applications.get(position);
        PackageManager pm = this.context.getPackageManager();
        String singleAppPackageName = appInfo.packageName;
        
        holder.appText.setText(pm.getApplicationLabel(appInfo));
        try {
            holder.appImage.setImageDrawable(pm.getApplicationIcon(singleAppPackageName));
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "onBindViewHolder: failure to draw icon");
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
        return this.applications.size();
    }

    @Subscribe
    public void onAppUnselected(BannedAppRemoved event) {
        notifyDataSetChanged();

    }

}

