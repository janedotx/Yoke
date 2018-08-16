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

import com.google.android.gms.common.util.CrashUtils;
import com.system2override.yoke.Models.BannedApps;
import com.system2override.yoke.MyApplication;
import com.system2override.yoke.R;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BannedAppAdapter extends RecyclerView.Adapter<BannedAppViewHolder>{
    private static final String TAG = "BannedAppAdapter";
    private List<ApplicationInfo> applications;
    private Context context;

    public BannedAppAdapter(Context context, List<ApplicationInfo> list) {
        this.applications = list;
        this.context = context;
    }

    @NonNull
    @Override
    public BannedAppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_detail, parent, false);
        return new BannedAppViewHolder(v, this.context, new BannedAppViewHolder.BannedAppClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(BannedAppAdapter.this.context, "clikced me", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onClick: was fucking clicked");
                String appName = BannedAppAdapter.this.applications.get(position).packageName;

                if (MyApplication.getBannedApps().getApps().contains(appName)) {
                    MyApplication.getBannedApps().removeApp(appName);
                    ((CheckBox)view.findViewById(R.id.singleAppCheckBox)).setChecked(false);
                } else {
                    MyApplication.getBannedApps().addApp(appName);
                    ((CheckBox)view.findViewById(R.id.singleAppCheckBox)).setChecked(true);
                }

                Set<String> apps = MyApplication.getBannedApps().getApps();
                Iterator<String> it = apps.iterator();
                while (it.hasNext()) {
                    Log.d(TAG, "onClick: bannedapp " + it.next());

                }
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull BannedAppViewHolder holder, int position) {
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


}

